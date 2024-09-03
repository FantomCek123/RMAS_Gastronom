package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastronomad2.models.entities.Restaurant

@Composable
fun DisplayRestaurants(restaurants: List<Restaurant?>?, navController: NavController,message:String) {
    if (restaurants == null) {
        Text(text = message, modifier = Modifier.padding(16.dp))
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            //.height(400.dp)
        ) {
            restaurants.forEach{ restaurant ->
                if (restaurant != null) {
                    RestaurantItem(restaurant, navController)
                }
            }
        }
    }
}