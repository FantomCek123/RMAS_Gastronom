package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.servises.implementations.LocationService.Companion.locationUpdates
import com.example.gastronomad2.ui.screens.createRestaurant.CreateRestaurantViewModel
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

@Composable
fun AddRestaurantLocationMap(
    onMapLongClick:(LatLng)->Unit,
){

    val currentLocation by locationUpdates.collectAsState()

    val uiSettings by remember { mutableStateOf(
        MapUiSettings(
        zoomControlsEnabled = true,
        mapToolbarEnabled = true,
        scrollGesturesEnabled = true,
        tiltGesturesEnabled = false,
        scrollGesturesEnabledDuringRotateOrZoom = false,
        zoomGesturesEnabled = true,
        myLocationButtonEnabled = true,
        compassEnabled = true,
        rotationGesturesEnabled = true,
        indoorLevelPickerEnabled = false
    )
    ) }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    val cameraPositionState by remember {
        mutableStateOf(
            CameraPositionState(
                CameraPosition(
                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                    15f,
                    0f,
                    0f
                )
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = {latLng -> onMapLongClick(latLng)},
        onPOIClick = {POI -> onMapLongClick(POI.latLng)}
    )
}
@Composable
fun NewRestaurantLocationPreviewMap(navController: NavController, title: String) {

    val currentLocation by locationUpdates.collectAsState()

    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                mapToolbarEnabled = false,
                scrollGesturesEnabled = true,
                tiltGesturesEnabled = false,
                scrollGesturesEnabledDuringRotateOrZoom = false,
                zoomGesturesEnabled = true,
                myLocationButtonEnabled = true,
                compassEnabled = true,
                rotationGesturesEnabled = true,
                indoorLevelPickerEnabled = false
            )
        )
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.NORMAL,
            )
        )
    }

    val cameraPositionState by remember {
        mutableStateOf(
            CameraPositionState(
                CameraPosition(
                    LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                    15f,
                    0f,
                    0f
                )
            )
        )
    }

    GoogleMap(
        modifier = Modifier
            .height(350.dp)
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapLongClick = { navController.navigate(Screen.AddRestaurantMapLocation.name) }
    ) {
        Marker(
            state = MarkerState(LatLng(currentLocation!!.latitude, currentLocation!!.longitude)),
            title = title, // Naslov koji korisnik unosi
            // snippet = cevm.description.value // Snipet koji korisnik unosi
            icon = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_BLUE
            )
        )
    }
}

@Composable
fun RestaurantLocationPreviewMap(restaurant: Restaurant, Height:Int = 250){

    val currentLocation by locationUpdates.collectAsState()


    val uiSettings by remember { mutableStateOf(
        MapUiSettings(
        zoomControlsEnabled = true,
        mapToolbarEnabled = false,
        scrollGesturesEnabled = true,
        tiltGesturesEnabled = false,
        scrollGesturesEnabledDuringRotateOrZoom = false,
        zoomGesturesEnabled = true,
        myLocationButtonEnabled = true,
        compassEnabled = true,
        rotationGesturesEnabled = true,
        indoorLevelPickerEnabled = false
    )
    ) }
    val properties by remember { mutableStateOf(
        MapProperties(
        mapType = MapType.NORMAL,
    )
    ) }

    val cameraPositionState by remember { mutableStateOf(
        CameraPositionState(
            CameraPosition(
                LatLng(restaurant.geoLocation!!.latitude, restaurant.geoLocation.longitude),
                15f,
                0f,
                0f
            )
        )
    )
    }

    GoogleMap(
        modifier = Modifier
            .height(Height.dp)
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
    ){
        Marker(
            state = MarkerState(LatLng(restaurant.geoLocation!!.latitude, restaurant.geoLocation.longitude)),
            title = restaurant.title,
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )

        Marker(
            state = MarkerState(position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)),
            title = "You"
        )
    }
}