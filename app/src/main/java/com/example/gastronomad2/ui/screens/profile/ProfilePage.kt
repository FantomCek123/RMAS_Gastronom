package com.example.gastronomad2.ui.screens.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.screens.components.NavigationBar
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DrawProfilePage(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    navBar: NavigationBar,
    dppvm: DrawProfilePageViewModel = DrawProfilePageViewModel.getInstance()
) {
    dppvm.loadProfilePicture()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                // Profile Picture
                if (dppvm.profilna != null) {
                    Image(
                        painter = rememberAsyncImagePainter(dppvm.profilna),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(CircleShape)
                            .border(
                                border = BorderStroke(2.dp, Color.Cyan),
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(128.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Username
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    dppvm.currentUserInfo?.userName?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // First Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                ) {
                    Text(
                        text = "Ime: " + dppvm.currentUserInfo?.firstName,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Last Name and IconButton
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Text(
                            text = "Prezime: " + dppvm.currentUserInfo?.lastName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                        )
                    }

                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Dodaj restoran",
                        modifier = Modifier
                            .size(24.dp) // Adjust size as needed
                            .padding(end = 8.dp) // Space between icon and text
                            .clickable { navController.navigate(Screen.CreateRestaurant.name) }
                    )
                    Text(
                        text = "Dodaj restoran",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable { navController.navigate(Screen.CreateRestaurant.name) }
                    )

                }

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Moji restorani",
                        modifier = Modifier
                            .size(24.dp) // Adjust size as needed
                            .padding(end = 8.dp) // Space between icon and text
                            .clickable { navController.navigate(Screen.MyRestaurants.name) }
                    )
                    Text(
                        text = "Moji restorani",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable { navController.navigate(Screen.MyRestaurants.name) }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    RadioButton(selected = dppvm.selected.value, onClick = {
                        dppvm.selected.value = !dppvm.selected.value
                        //setForground()
                    })
                    Text(
                        text = "Omoguci pracenje:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.clickable { navController.navigate(Screen.MyRestaurants.name) }
                    )
                }

                Button(
                    onClick = {
                        dppvm.signOut(
                            context = context,
                            onSuccess = {
                                navController.popBackStack(Screen.SignIn.name, inclusive = true)
                                navController.navigate(Screen.SignIn.name)
                            },
                            onError = { /* Handle error */ }
                        )
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Sign Out")
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                navBar.Draw(
                    currentScreen = Screen.Profile // Example currentScreen, replace with actual state
                )
            }
        }
    }
}
