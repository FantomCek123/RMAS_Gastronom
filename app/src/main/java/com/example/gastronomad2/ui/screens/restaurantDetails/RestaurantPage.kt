package com.example.gastronomad2.ui.screens.restaurantDetails

import android.content.Context
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.db.implementations.MediaDbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.screens.components.CommentsSection
import com.example.gastronomad2.ui.screens.components.RestaurantLocationPreviewMap
import com.example.gastronomad2.ui.theme.Purple40
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

@Composable
fun DrawRestaurantPage(
    restaurant: Restaurant,
    navController: NavController,
    restaurantPageViewModel: RestaurantPageViewModel = RestaurantPageViewModel.getInstance(),
    mDbApi: MediaDbApi = MediaDbApi(),
    dbApi: DbApi = DbApi(),
    context: Context
) {
    LaunchedEffect(restaurant) {
        restaurantPageViewModel.toggleCommentsVisibility()
        restaurantPageViewModel.restaurantPictures.value = emptyList()
        mDbApi.downloadRestaurantPictures(uid = restaurant.publisherId!!, rid = restaurant.id!!)
    }
    val currentRestaurant = restaurantPageViewModel.restaurant

    val number = restaurantPageViewModel.ratingInput.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(36.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(
                    BorderStroke(6.dp, MaterialTheme.colorScheme.primary)
                )
        ) {
            items(restaurantPageViewModel.restaurantPictures.value) { uri ->
                AsyncImage(
                    model = uri,
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(8.dp)
        ) {
            // Text za prikaz naslova restorana
            Text(
                text = "${currentRestaurant.title}",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 10.dp).weight(1f),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )

            Spacer(modifier = Modifier.weight(1f)) // Ovaj Spacer će popuniti preostali prostor i pomeriti ikonu na desnu stranu

            // Ikona zvezde sa prosečnom ocenom
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { restaurantPageViewModel.showDialog() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "Star Icon",
                        tint = Color.Yellow,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }//currentRestaurant.rating
                Text(
                    text = "${String.format("%.2f", currentRestaurant.rating)}", // Prikaz prosečne ocene sa dve decimale
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 20.sp // Postavi željenu veličinu fonta
                    ),
                    modifier = Modifier.padding(end = 8.dp) // Razmak između ocene i ikone
                )

                Text(
                    text = "(${currentRestaurant.numOfRates})", // Prikaz prosečne ocene sa dve decimale
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp, // Postavi željenu veličinu fonta
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(end = 4.dp) // Razmak između ocene i ikone
                )
            }
        }

        Text(
            text = "    Opis:",
            fontSize = 18.sp,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${currentRestaurant.description}",
                    fontSize = 15.sp,
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }

        Text(
            text = "Adresa: ${currentRestaurant.location}",
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 12.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(8.dp)
        ) {
            Spacer(modifier = Modifier.width(12.dp)) // Razmak sa leve strane

            Text(
                text = DbApi().getUser(restaurant.publisherId!!)!!.userName,
                fontSize = 18.sp,
            )

            Spacer(modifier = Modifier.weight(1f)) // Ovaj Spacer će ispuniti preostali prostor i gurati IconButton na desno

            if (CurrentUserInfo.getInstance().get()!!.id == restaurant.publisherId) {
                IconButton(onClick = {
                    dbApi.deleteRestaurant(restaurant.id!!)
                    navController.popBackStack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
            }
        }

        RestaurantLocationPreviewMap(restaurant = currentRestaurant)
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { restaurantPageViewModel.loadComments() }) {
                Icon(
                    painter = painterResource(R.drawable.message),
                    contentDescription = null,
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp),
                )
            }
        }
        if (restaurantPageViewModel.showComments) {
            CommentsSection(restaurantPageViewModel.comments)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = restaurantPageViewModel.newCommentText,
                onValueChange = { restaurantPageViewModel.newCommentText = it },
                label = { Text("Novi komentar") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    restaurantPageViewModel.addComment()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Dodaj komentar")
            }
        }
        Spacer(modifier = Modifier.height(90.dp))
    }

    // Dodaj prikaz dijaloga
    if (restaurantPageViewModel.showRatingDialog) {
        AlertDialog(
            onDismissRequest = { restaurantPageViewModel.hideDialog() },
            title = { Text(text = "Oceni restoran") },
            text = {
                Column {
                    Text(text = "Unesi ocenu od 1 do 10:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        singleLine = true,
                        value = number.value,
                        onValueChange = {
                            if(isValidRating(it))
                            restaurantPageViewModel.updateNumberRating(it) },
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Pozovi suspend funkciju koristeći LaunchedEffect
                            restaurantPageViewModel.submitRating()
                        restaurantPageViewModel.hideDialog()
                    }
                ) {
                    Text("Potvrdi")
                }
            },
            dismissButton = {
                Button(
                    onClick = { restaurantPageViewModel.hideDialog() }
                ) {
                    Text("Otkaz")
                }
            }
        )
    }
}

fun isValidRating(ratingString: String): Boolean {
    // Proveri da li je string prazan
    if (ratingString.isEmpty()) {
        return true
    }

    // Pokušaj da pretvoriš string u broj
    val rating = ratingString.toIntOrNull()

    // Proveri da li je broj u opsegu od 1 do 10
    return rating != null && rating in 1..10
}