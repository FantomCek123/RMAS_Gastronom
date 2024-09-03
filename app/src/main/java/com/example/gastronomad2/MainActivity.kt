package com.example.gastronomad2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
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
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.servises.implementations.LocationService
import com.example.gastronomad2.servises.test.RunningService
import com.example.gastronomad2.ui.screens.LoadingScreen
import com.example.gastronomad2.ui.screens.components.AddRestaurantLocationMap
import com.example.gastronomad2.ui.screens.components.NavigationBar
import com.example.gastronomad2.ui.screens.createRestaurant.CreateRestaurantViewModel
import com.example.gastronomad2.ui.screens.createRestaurant.DrawCreateRestaurantPage
import com.example.gastronomad2.ui.screens.filteredRestaurants.DrawFilteredRestaurants
import com.example.gastronomad2.ui.screens.filters.DrawFilterPage
import com.example.gastronomad2.ui.screens.myRestaurants.DrawMyRestaurants
import com.example.gastronomad2.ui.screens.profile.DrawProfilePage
import com.example.gastronomad2.ui.screens.restaurantDetails.DrawRestaurantPage
import com.example.gastronomad2.ui.screens.restaurantDetails.RestaurantPageViewModel
import com.example.gastronomad2.ui.screens.splash.SplashScreen
import com.google.gson.Gson


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /////// ZA PRACENJE KKORISNIKA ///////////////
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )

        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_START
            startService(this)
        }
        /////// ZA PRACENJE KKORISNIKA ///////////////


        ///////////// TEST /////////////// RADI
/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val chanel = NotificationChannel(
                "running_channel",
                "Running Notification",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationMenager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationMenager.createNotificationChannel(chanel)
        }


        if(!isForgroundActive.value)
            Intent(applicationContext, RunningService::class.java).apply {
                action = RunningService.START
                startService(this)
            }

        else{
            Intent(applicationContext, RunningService::class.java).apply {
                action = RunningService.STOP
                startService(this)
            }
        }
        ///////////// TEST ///////////////
*/

        enableEdgeToEdge()
        setContent {
            GastronomadApp(this)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Intent(applicationContext, LocationService::class.java).apply {
            action = LocationService.ACTION_STOP
            startService(this)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GastronomadApp(context: Context) {

    val navController = rememberNavController()
    val createRestaurantVM = CreateRestaurantViewModel.getInstance()

    val navigationBar = NavigationBar(
        navigateToHomePage = {
            navController.popBackStack(Screen.Home.name, inclusive = true)
            navController.navigate(Screen.Home.name)
        },
        navigateToProfilePage = {
            navController.popBackStack(Screen.Profile.name, inclusive = true)
            navController.navigate(Screen.Profile.name)
        },
        navigateToFilterPage = {
            navController.popBackStack(Screen.Filter.name, inclusive = true)
            navController.navigate(Screen.Filter.name)
        }

    )

    NavHost(navController = navController, startDestination = Screen.Splash.name) {
        composable(Screen.Splash.name) {
            SplashScreen(navController = navController)
        }
        composable(Screen.SignUp.name) {
            DrawSignUpPage(navController = navController, context = context)
        }
        composable(Screen.SignIn.name) {
            DrawSignInPage(navController = navController, context = context)
        }
        composable(Screen.Home.name) {
            DrawHomePage(navController = navController, context = context, navBar = navigationBar)
        }
        composable(Screen.ProfilePicture.name) {
            DrawChooseProfilePicturePage(navController = navController, context = context)
        }
        composable(Screen.Loading.name) {
            LoadingScreen()
        }
        composable(Screen.Profile.name) {
            DrawProfilePage(navController = navController, context = context, navBar = navigationBar)
        }
        composable(Screen.CreateRestaurant.name) {
            DrawCreateRestaurantPage(navController = navController, context = context)
        }
        composable(route = Screen.AddRestaurantMapLocation.name) {
            AddRestaurantLocationMap(
                onMapLongClick = { latLng ->
                    createRestaurantVM.setCoordinates(latLng)
                    navController.navigate(Screen.CreateRestaurant.name)
                }
            )
        }
        composable(Screen.MyRestaurants.name) {
            DrawMyRestaurants(navController = navController, context = context)
        }
        composable(Screen.RestaurantsDetails.name) {
            DrawRestaurantPage(navController = navController, context = context, restaurant = RestaurantPageViewModel.getInstance().restaurant)
        }
        composable(Screen.RestaurantsDetails.name) {
            DrawRestaurantPage(
                restaurant = RestaurantPageViewModel.getInstance().restaurant,
                navController = navController,
                context = context
            )
        }
        composable(Screen.Filter.name) {
           DrawFilterPage(navController = navController,navBar = navigationBar)
        }
        composable(Screen.FilteredRestaurants.name) {
            DrawFilteredRestaurants(navController = navController, context = context)
        }
    }
}

fun setForground()
{
    isForgroundActive.value = !isForgroundActive.value
}

var isForgroundActive = mutableStateOf(false)








