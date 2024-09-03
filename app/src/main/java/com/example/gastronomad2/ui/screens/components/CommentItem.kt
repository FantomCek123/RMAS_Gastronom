package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.Comment
import com.example.gastronomad2.ui.screens.restaurantDetails.RestaurantPageViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

@Composable
fun CommentItem(comment: Comment) {
    Column(modifier = Modifier
        .padding(12.dp)
        .background(
            shape = RoundedCornerShape(8.dp),
            color = Color.LightGray // prepraviti
        )
        .padding(16.dp)
        .fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "User: ${comment.username}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.weight(1f) // Rastezanje teksta da zauzme prostor do ikonice
            )
            Spacer(modifier = Modifier.width(8.dp)) // Prostor između teksta i ikonice
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = comment.text ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        //Spacer(modifier = Modifier.height(8.dp))
    }
}

