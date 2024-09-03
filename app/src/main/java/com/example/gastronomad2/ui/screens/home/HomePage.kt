package com.example.gastronomad2.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.implementations.LocationService
import com.example.gastronomad2.servises.implementations.LocationService.Companion.locationUpdates
import com.example.gastronomad2.ui.screens.components.NavigationBar
import com.google.android.gms.maps.CameraUpdateFactory
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DrawHomePage(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    navBar: NavigationBar
) {

    val currentLocation = locationUpdates.collectAsState().value
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(currentLocation?.latitude ?: 10.0, currentLocation?.longitude ?: 10.0), 15f
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // GoogleMap and Text placed at the top
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp) // Adjust to accommodate navBar height
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                cameraPositionState = cameraPositionState
            ) {
                currentLocation?.let {
                    Marker(
                        state = MarkerState(position = LatLng(it.latitude, it.longitude)),
                        title = "Current Location"
                    )
                }
            }

            Text(
                text = "Location: ${currentLocation?.latitude}, ${currentLocation?.longitude}",
                fontSize = 16.sp,
                modifier = modifier.padding(0.dp, 6.dp)
            )
        }

        // NavigationBar positioned at the bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            navBar.Draw(
                currentScreen = Screen.Home // Example currentScreen, replace with actual state
            )
        }
    }
}