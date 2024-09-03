package com.example.gastronomad2.ui.screens.splash

import androidx.navigation.NavController
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.ui.GastonomadAppViewModel

class SplashViewModel (
    private val accountService: AccountServise,
) : GastonomadAppViewModel(){

    companion object{

        private var INSTANCE: SplashViewModel? = null

        fun getInstance(): SplashViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SplashViewModel(accountService = AccountServise()).also { INSTANCE = it }
            }
        }
    }

    fun onAppStart(navController: NavController) {
        if (accountService.hasUser()){
            navController.popBackStack(Screen.Home.name, inclusive = true)
            navController.navigate(Screen.Home.name)
        }
        else {
            navController.popBackStack(Screen.Splash.name, inclusive = true)
            navController.navigate(Screen.SignIn.name)
        }
    }
}