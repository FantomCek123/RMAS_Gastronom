package com.example.gastronomad2.ui.screens.filters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.RestaurantType
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.example.gastronomad2.ui.screens.filteredRestaurants.FilteredRestaurantsViewModel

class FilterPageViewModel private constructor() : GastonomadAppViewModel() {

    var restaurantName = mutableStateOf("")

    var minimumRating = mutableStateOf<Double?>(null)

    var expandedRating = mutableStateOf(false)

    var expandedType = mutableStateOf(false)

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
    }

    fun applayFilters()
    {

        var namededRestaurants: List<Restaurant?> = emptyList()
        var typedRestaurants: List<Restaurant?> = emptyList()
        var ratedRestaurants: List<Restaurant?> = emptyList()

        if (restaurantName.value.isNotEmpty()) {
            namededRestaurants = DbApi().getRestaurantsWithTitle(restaurantName.value)
        }
        if (selectedType.value != null) {
            typedRestaurants = DbApi().getRestaurantsWithTypes(listOf(selectedType.value!!.name))
        }
        if (minimumRating.value != null) {
            ratedRestaurants = DbApi().getRestaurantsWithRating(minimumRating.value)
        }

        val filteredRestaurants = when {
            namededRestaurants.isNotEmpty() && typedRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() -> {
                namededRestaurants
                    .intersect(typedRestaurants.toSet())
                    .intersect(ratedRestaurants.toSet())
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

            typedRestaurants.isNotEmpty() && ratedRestaurants.isNotEmpty() -> {
                typedRestaurants
                    .intersect(ratedRestaurants.toSet())
                    .toList()
            }
            namededRestaurants.isNotEmpty() -> namededRestaurants.toList()
            typedRestaurants.isNotEmpty() -> typedRestaurants.toList()
            ratedRestaurants.isNotEmpty() -> ratedRestaurants.toList()
            else -> emptyList()
        }

        FilteredRestaurantsViewModel.getInstance().setRestaurants(filteredRestaurants)

        clearFilters()
    }

}