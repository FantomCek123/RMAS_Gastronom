package com.example.gastronomad2.ui.screens.signUpPage

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.db.implementations.MediaDbApi
import com.example.gastronomad2.servises.implementations.AccountServise
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/* DODATI DODANE ATRIBUTE ZA USERA IPOGLEDATI F-JU onSignUpClick */
class SignUpPageViewModel private constructor (
    context: Context,
    private val accountService: AccountServise) : GastonomadAppViewModel()
{

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val userName = MutableStateFlow("")
    val number = MutableStateFlow("")
    var profilePicture by mutableStateOf<Uri?>(null)


    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword.value = newConfirmPassword
    }

    fun updateFirstName(newFirstName: String) {
        firstName.value = newFirstName
    }

    fun updateLastName(newLastName: String) {
        lastName.value = newLastName
    }

    fun updateUserName(newUserName: String) {
        userName.value = newUserName
    }

    fun updateNumber(newNumber: String) {
        number.value = newNumber
    }

    /////////////////////////////////////////////////////


    val appContext = context


    companion object{
        private var INSTANCE: SignUpPageViewModel? = null

        fun getInstance(context:Context): SignUpPageViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SignUpPageViewModel(accountService = AccountServise(),
                    context = context
                ).also { INSTANCE = it }
            }
        }
    }


    //////////////////////////////////////////////////////

    fun onSignUpClick(
        navController: NavController,
        loadingScreen: String,
        openAndPopUp: () -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Navigacija na ekran za učitavanje
                navController.navigate(loadingScreen)

                // Validacija unosa
                val validationResult = withContext(Dispatchers.Default) {
                    if (email.value.isEmpty() || password.value.isEmpty() || confirmPassword.value.isEmpty() ||
                        firstName.value.isEmpty() || lastName.value.isEmpty() || number.value.isEmpty() || profilePicture == null
                    ) {
                        "Popunite sva polja!"
                    } else if (password.value.length < 8) {
                        "Šifra mora biti duža od 8 karaktera!"
                    } else if (password.value != confirmPassword.value) {
                        "Šifre se ne podudaraju!"
                    } else if (!isPhoneNumber(number.value)) {
                        "Nevalidan broj!"
                    } else if (DbApi().getUserWithUsername(userName.value)) {
                        "Postoji korisnik sa istim username-om!"
                    } else {
                        null // Sve je u redu
                    }
                }

                if (validationResult != null) {
                    Toast.makeText(appContext, validationResult, Toast.LENGTH_SHORT).show()
                } else {
                    // Ako su svi uslovi zadovoljeni, pokušaj registraciju
                    val result = withContext(Dispatchers.IO) {
                        accountService.signUp(
                            email.value,
                            password.value, firstName.value, lastName.value,
                            number.value, profilePicture!!, userName.value
                        )
                    }

                    // Prikazi poruku o rezultatu
                    Toast.makeText(appContext, result, Toast.LENGTH_SHORT).show()

                    if (result == "Uspesno ste kreirali nalog!") {
                        openAndPopUp() // Poziv funkcije za otvaranje novog ekrana
                    } else {
                        // Ako registracija nije uspešna, možeš prikazati poruku o grešci
                        Toast.makeText(appContext, "Registracija neuspešna!", Toast.LENGTH_SHORT).show()
                    }
                }
            } finally {
                // Odlazak sa strane za učitavanje nakon završetka signUp funkcije
                navController.popBackStack(loadingScreen, inclusive = true)
            }
        }
    }

    fun isPhoneNumber(input: String): Boolean {
        val regex = Regex("^[0-9]+$")
        return regex.matches(input)
    }
}