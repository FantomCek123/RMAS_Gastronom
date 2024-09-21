package com.example.gastronomad2.ui.screens.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.servises.implementations.LocationService
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class DrawProfilePageViewModel private constructor() : GastonomadAppViewModel() {
    companion object {
        private var INSTANCE: DrawProfilePageViewModel? = null

        fun getInstance(): DrawProfilePageViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: DrawProfilePageViewModel().also { INSTANCE = it }
            }
        }
    }


    private val accService: AccountServise = AccountServise()
    val dbApi: DbApi = DbApi()
    var currentUserInfo = mutableStateOf(DbApi().getUser(Firebase.auth.currentUser!!.uid)!!)


    var profilna by mutableStateOf<Uri?>(null)
    var isProfilePictureLoaded by mutableStateOf(false)
    var isServiceActive by mutableStateOf(false)

    fun setProfilePicture(uri: Uri?) {
        this.profilna = uri
    }

    fun loadProfilePicture() {
        if (!isProfilePictureLoaded) {
            dbApi.downloadProfilePicture(currentUserInfo.value!!.id)
            isProfilePictureLoaded = true
        }
    }

    var selected = mutableStateOf(false)

    fun signOut(context: Context, onSuccess: () -> Unit, onError: (Exception) -> Unit) { // Dodato prosleđivanje konteksta
        launchCatching {
            try {
                accService.signOut(context)
                clearAppData(context) // Pozivanje funkcije za brisanje podataka
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    private fun clearAppData(context: Context) { // Dodata funkcija za brisanje podataka aplikacije
        try {
            val packageName = context.packageName
            val runtime = Runtime.getRuntime()
            //runtime.exec("pm clear $packageName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setLocationService(appContext:Context) {
        if(isServiceActive)
        {
            Intent(appContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START_NEARBY
                appContext.startForegroundService(this)
            }
        }
        else {
            Intent(appContext, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                appContext.stopService(this)
            }
            Intent(appContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                appContext.startForegroundService(this)
            }
        }
    }

    fun setInfoForUser() {
     //   currentUserInfo.value = DbApi().getUser(Firebase.auth.currentUser!!.uid)!!
    }



}