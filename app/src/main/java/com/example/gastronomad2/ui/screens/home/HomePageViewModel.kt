package com.example.gastronomad2.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.servises.implementations.LocationService
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.example.gastronomad2.ui.screens.signUpPage.SignUpPageViewModel
import com.google.android.gms.maps.model.LatLng
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.screens.home.DrawHomePage
import com.example.gastronomad2.ui.screens.signInPage.DrawSignInPage
import com.example.gastronomad2.ui.screens.signUpPage.DrawChooseProfilePicturePage
import com.example.gastronomad2.ui.screens.signUpPage.DrawSignUpPage
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.models.entities.Comment
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.servises.implementations.LocationService.Companion.locationUpdates
import com.example.gastronomad2.ui.theme.BackgroundLocationTrackingTheme
import kotlinx.coroutines.flow.MutableStateFlow


class HomePageViewModel private constructor() : GastonomadAppViewModel() {

    companion object {
        private var INSTANCE: HomePageViewModel? = null

        fun getInstance(): HomePageViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomePageViewModel().also { INSTANCE = it }
            }
        }
    }
  //  var users by mutableStateOf(listOf<User?>())
//        private set
    val dbApi:DbApi = DbApi()

    var restaurants = mutableStateListOf<Restaurant>()
        private set

    fun setUsers() : List<User?>
    {
        return dbApi.getTopUsersByPoints()
    }

    val currentLocation = MutableStateFlow(locationUpdates)

    //val currentLocation by locationUpdates.collectAsState()



    fun getRestayrants()
    {
        restaurants.clear()
        restaurants.addAll(dbApi.getNearbyRestaurants(currentLocation.value.value!!.latitude,
            currentLocation.value.value!!.longitude,4.5))
    }



}