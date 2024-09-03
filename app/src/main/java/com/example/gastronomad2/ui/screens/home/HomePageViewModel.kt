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
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.ui.theme.BackgroundLocationTrackingTheme


class HomePageViewModel private constructor(context: Context) : GastonomadAppViewModel() {

    companion object {
        private var INSTANCE: HomePageViewModel? = null

        fun getInstance(context: Context): HomePageViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomePageViewModel(context).also { INSTANCE = it }
            }
        }
    }

    fun kurac(applicationContext:Context)
    {
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            applicationContext.startService(this)
        }
    }
}