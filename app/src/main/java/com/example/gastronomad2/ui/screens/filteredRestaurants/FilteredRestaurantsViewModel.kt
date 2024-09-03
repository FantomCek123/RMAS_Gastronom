package com.example.gastronomad2.ui.screens.filteredRestaurants

import androidx.compose.runtime.mutableStateOf
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.ui.GastonomadAppViewModel

class FilteredRestaurantsViewModel private constructor(): GastonomadAppViewModel() {

    companion object {
        private var INSTANCE: FilteredRestaurantsViewModel? = null

        fun getInstance(): FilteredRestaurantsViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FilteredRestaurantsViewModel().also { INSTANCE = it }
            }
        }
    }

    var filteredRestaurants = mutableStateOf<List<Restaurant?>>(emptyList())
        private set


    fun setRestaurants(restaurants: List<Restaurant?>) {
        filteredRestaurants.value = restaurants
    }

    val listOfSortsMethods = listOf(
        "Sortiraj po nazivu", "Sortiraj po oceni opadajuci",
        "Sortiraj po oceni rastucoj", "Najnovije", "Najstarije"
    )

    var expanded = mutableStateOf(false)

    fun sortRestaurants(sortMethod: String) {
        when (sortMethod) {
            "Sortiraj po nazivu" -> setRestaurants(filteredRestaurants.value.sortedBy { it?.title ?: "" })
            "Sortiraj po oceni opadajuci" -> setRestaurants(filteredRestaurants.value.sortedByDescending { it?.rating })
            "Sortiraj po oceni rastucoj" -> setRestaurants(filteredRestaurants.value.sortedBy { it?.rating })
            "Najnovije" -> setRestaurants(filteredRestaurants.value.sortedByDescending { it?.date })
            "Najstarije" -> setRestaurants(filteredRestaurants.value.sortedBy { it?.date })

        }
    }
}
