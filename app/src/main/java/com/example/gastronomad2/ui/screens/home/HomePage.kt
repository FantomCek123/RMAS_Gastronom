package com.example.gastronomad2.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.servises.implementations.LocationService
import com.example.gastronomad2.servises.implementations.LocationService.Companion.locationUpdates
import com.example.gastronomad2.ui.screens.components.DisplayRatings
import com.example.gastronomad2.ui.screens.components.NavigationBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun DrawHomePage(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    navBar: NavigationBar
) {
    val hpvm: HomePageViewModel = HomePageViewModel.getInstance()
    val currentLocation by locationUpdates.collectAsState()

    // Track the loading state
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(currentLocation?.latitude ?: 10.0, currentLocation?.longitude ?: 10.0), 15f
        )
    }

    hpvm.getRestayrants()

    Scaffold(
        bottomBar = {
            navBar.Draw(currentScreen = Screen.Home)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    currentLocation?.let {
                        Marker(
                            state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                            title = "Current Location"
                        )
                    }
                    if (hpvm.restaurants.isNotEmpty()) {
                        hpvm.restaurants.forEach { restaurant ->
                            Marker(
                                state = MarkerState(
                                    position = LatLng(
                                        restaurant.geoLocation!!.latitude,
                                        restaurant.geoLocation.longitude
                                    )
                                ),
                                title = restaurant.title,
                                icon = BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_BLUE
                                ) // Blue for restaurants
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // To take the remaining space after the GoogleMap
                    .border(width = 2.dp, color = Color.Gray) // Border added
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp)
                        ) {
                            DisplayRatings(users = hpvm.setUsers())
                        }
                    }
                }
            }
        }
    }
}

