package com.example.gastronomad2.ui.screens.signInPage

import android.content.Context
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.navigation.NavController
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SignInViewModel private constructor(
    private val accountService: AccountServise,
    context: Context
    ) : GastonomadAppViewModel() {
    val appContext = context

    companion object {

        private var INSTANCE: SignInViewModel? = null

        fun getInstance(context: Context): SignInViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SignInViewModel(
                    accountService = AccountServise(),
                    context
                ).also { INSTANCE = it }
            }
        }
    }

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible

    fun togglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }


    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick(
        navController: NavController,
        loadingScreen: String,
        openAndPopUp: () -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Navigacija na stranu za učitavanje
                navController.navigate(loadingScreen)

                // Poziv funkcije signIn i čekanje njenog završetka
                val success = withContext(Dispatchers.IO) {
                    accountService.signIn(email.value, password.value)
                }

                if (success) {
                    makeText(appContext, "Uspeh!", LENGTH_SHORT).show()
                    CurrentUserInfo.getInstance().get()
                    openAndPopUp()
                    //CurrentUserInfo.getInstance().user = DbApi().getUserWithEmail(email.value)
                } else {
                    makeText(appContext, "Greska!", LENGTH_SHORT).show()
                }

            } finally {
                // Odlazak sa strane za učitavanje nakon završetka signIn funkcije
                navController.popBackStack(loadingScreen, inclusive = true)
            }
        }
    }
}