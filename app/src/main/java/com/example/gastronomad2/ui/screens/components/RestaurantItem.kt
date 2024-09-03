package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.screens.restaurantDetails.RestaurantPageViewModel

@Composable
fun RestaurantItem(restaurant: Restaurant,navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .padding(16.dp)
            .clickable {
                // Izvršava se kada se klikne na Box
                RestaurantPageViewModel.getInstance().updateRestaurant(restaurant)
                navController.popBackStack(Screen.RestaurantsDetails.name, inclusive = true)
                navController.navigate(Screen.RestaurantsDetails.name)
            }
    ) {
        Column() {
            Text(
                text = "${restaurant.title}",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacing between title and other details
            Text(
                text = "Lokacija: ${restaurant.location}",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp)) // Spacing before like and comment icons
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Star, // Koristi standardnu ikonu zvezde iz Material Designa
                    contentDescription = "Star Icon", // Opis za pristupačnost
                    //modifier = modifier.size(24.dp), // Veličina ikone
                    tint = Color.Yellow // Boja ikone
                )
                Text(
                    text = roundToTwoDecimals(restaurant.rating),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

fun roundToTwoDecimals(value: Double): String {
    return String.format("%.2f", value)
}