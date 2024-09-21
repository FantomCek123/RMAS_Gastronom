package com.example.gastronomad2.ui.screens.filters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.RestaurantType
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.example.gastronomad2.ui.screens.filteredRestaurants.FilteredRestaurantsViewModel

class FilterPageViewModel private constructor() : GastonomadAppViewModel() {

    var restaurantName = mutableStateOf("")

    var minimumRating = mutableStateOf<Double?>(null)

    var expandedRating = mutableStateOf(false)

    var expandedType = mutableStateOf(false)

    var radius = mutableStateOf(0.00)

    companion object {
        private var INSTANCE: FilterPageViewModel? = null

        fun getInstance(): FilterPageViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FilterPageViewModel().also { INSTANCE = it }
            }
        }
    }

    var selectedType = mutableStateOf<RestaurantType?>(null)

    var ratingOptions = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val restaurantTypes = RestaurantType.entries

    fun clearFilters()
    {
        restaurantName.value = ""
        minimumRating.value = null
        expandedRating.value = false
        expandedType.value = false
        selectedType.value = null
        radius.value = 0.00
    }

    fun applayFilters()
    {

        var namededRestaurants: List<Restaurant?> = emptyList()
        var typedRestaurants: List<Restaurant?> = emptyList()
        var ratedRestaurants: List<Restaurant?> = emptyList()
        var nearbyRestaurants: List<Restaurant?> = emptyList()

        if (restaurantName.value.isNotEmpty()) {
            namededRestaurants = DbApi().getRestaurantsWithTitle(restaurantName.value)
        }
        if (selectedType.value != null) {
            typedRestaurants = DbApi().getRestaurantsWithTypes(listOf(selectedType.value!!.name))
        }
        if (minimumRating.value != null) {
            ratedRestaurants = DbApi().getRestaurantsWithRating(minimumRating.value)
        }
        if (radius.value != 0.00)
        {
            nearbyRestaurants = DbApi().getNearbyRestaurants(CurrentUserInfo.getInstance().get()!!.latLng!!.latitude,
                CurrentUserInfo.getInstance().get()!!.latLng!!.longitude,radius.value)
        }

                val filteredRestaurants = when {
                    namededRestaurants.isNotEmpty() && typedRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                        namededRestaurants
                            .intersect(typedRestaurants.toSet())
                            .intersect(ratedRestaurants.toSet())
                            .intersect(nearbyRestaurants.toSet())
                            .toList()
                    }
                    namededRestaurants.isNotEmpty() && typedRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() -> {
                        namededRestaurants
                            .intersect(typedRestaurants.toSet())
                            .intersect(ratedRestaurants.toSet())
                            .toList()
                    }
                    namededRestaurants.isNotEmpty() && typedRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                        namededRestaurants
                            .intersect(typedRestaurants.toSet())
                            .intersect(nearbyRestaurants.toSet())
                            .toList()
                    }
                    namededRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                        namededRestaurants
                            .intersect(ratedRestaurants.toSet())
                            .intersect(nearbyRestaurants.toSet())
                            .toList()
                    }
                    typedRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                        typedRestaurants
                            .intersect(ratedRestaurants.toSet())
                            .intersect(nearbyRestaurants.toSet())
                            .toList()
            }
            namededRestaurants.isNotEmpty() && typedRestaurants.isNotEmpty() -> {
                namededRestaurants
                    .intersect(typedRestaurants.toSet())
                    .toList()
            }
            namededRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() -> {
                namededRestaurants
                    .intersect(ratedRestaurants.toSet())
                    .toList()
            }
            namededRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                namededRestaurants
                    .intersect(nearbyRestaurants.toSet())
                    .toList()
            }
            typedRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() -> {
                typedRestaurants
                    .intersect(ratedRestaurants.toSet())
                    .toList()
            }
            typedRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                typedRestaurants
                    .intersect(nearbyRestaurants.toSet())
                    .toList()
            }
            ratedRestaurants.isNotEmpty() && nearbyRestaurants.isNotEmpty() -> {
                ratedRestaurants
                    .intersect(nearbyRestaurants.toSet())
                    .toList()
            }
            namededRestaurants.isNotEmpty() -> namededRestaurants.toList()
            typedRestaurants.isNotEmpty() -> typedRestaurants.toList()
            ratedRestaurants.isNotEmpty() -> ratedRestaurants.toList()
            nearbyRestaurants.isNotEmpty() -> nearbyRestaurants.toList()
            else -> emptyList()
        }

        FilteredRestaurantsViewModel.getInstance().setRestaurants(filteredRestaurants)

        clearFilters()
    }

}