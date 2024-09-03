package com.example.gastronomad2.ui.screens.profile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.db.implementations.UserDbApi
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.example.gastronomad2.ui.screens.home.HomePageViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
    val currentUserInfo = CurrentUserInfo.getInstance().get()

    var profilna by mutableStateOf<Uri?>(null)
    var isProfilePictureLoaded by mutableStateOf(false)

    fun setProfilePicture(uri: Uri?) {
        this.profilna = uri
    }

    fun loadProfilePicture() {
        if (!isProfilePictureLoaded) {
            dbApi.downloadProfilePicture(currentUserInfo!!.id)
            isProfilePictureLoaded = true
        }
    }

    var selected = mutableStateOf(false)

    fun signOut(context: Context, onSuccess: () -> Unit, onError: (Exception) -> Unit) { // Dodato prosleđivanje konteksta
        launchCatching {
            try {
                accService.signOut()
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



}