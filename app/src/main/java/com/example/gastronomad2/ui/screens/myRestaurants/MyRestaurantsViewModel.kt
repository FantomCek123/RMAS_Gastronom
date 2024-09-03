package com.example.gastronomad2.ui.screens.myRestaurants

import androidx.compose.runtime.mutableStateOf
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.ui.GastonomadAppViewModel

class MyRestaurantsViewModel private constructor(): GastonomadAppViewModel() {

    companion object{
        private var INSTANCE: MyRestaurantsViewModel? = null

        fun getInstance(): MyRestaurantsViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MyRestaurantsViewModel().also { INSTANCE = it }
            }
        }
    }

    var myRestaurants  = mutableStateOf<List<Restaurant>>(emptyList())
        private set


    fun setRestaurants(restaurants : List<Restaurant>){
        myRestaurants.value = restaurants
    }


}

//    UserDbApi().getRestaurantsForUser()