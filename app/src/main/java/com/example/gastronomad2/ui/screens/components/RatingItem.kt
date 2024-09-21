package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.models.entities.User

@Composable
fun RatingItem(user: User) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .background(
                color = Color.LightGray, // Ako želiš promenu boje pozadine
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "User: ${user.userName}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.weight(1f) // Rastezanje teksta da zauzme prostor do ikonice
            )
            Spacer(modifier = Modifier.width(8.dp)) // Prostor između teksta i ikonice

            // Prikaz bodova i ikonica
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user.points}", // Prikaz bodova
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp)) // Prostor između bodova i ikonice
                Icon(
                    imageVector = Icons.Filled.Star, // Ikonica, možeš promeniti u bilo koju drugu
                    contentDescription = "Points",
                    tint = Color.Yellow // Boja ikonice
                )
            }
        }
    }
}