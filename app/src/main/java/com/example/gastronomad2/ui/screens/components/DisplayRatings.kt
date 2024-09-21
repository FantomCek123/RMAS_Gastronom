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
import com.example.gastronomad2.models.entities.User

@Composable
fun DisplayRatings(users: List<User?>?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        //.height(400.dp)
    ) {
        users?.forEach { user ->
            if (user != null) {
                RatingItem(user)
            }
        }
    }

}