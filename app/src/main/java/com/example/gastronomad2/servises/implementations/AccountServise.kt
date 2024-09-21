package com.example.gastronomad2.servises.implementations

import android.content.Context
import android.net.Uri
import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.db.implementations.MediaDbApi
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AccountServise {

    private val dbApi = DbApi()
    val mDbApi = MediaDbApi()

    suspend fun signIn(email: String, password: String):Boolean {
        try {
            Firebase.auth.signInWithEmailAndPassword(email, password).await()
            return true
        }
        catch (error: Exception){
            return false
        }
    }


    // DODATI ATRIBUTE!!!!
    suspend fun signUp(email: String, password: String,firstName: String,lastName: String,
    number: String,profilePicture: Uri,userName:String) :String {
        val userCredential = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
        signIn(email, password)
        //val user = userCredential.user
        //user?.sendEmailVerification()?.await()
        val id: String = Firebase.auth.currentUser!!.uid

        //dbApi.addUser2(Firebase.auth.currentUser!!.uid,email,username,name,surname)
        dbApi.addUser(
            User(
                id = id,
                email = email,
                firstName = firstName,
                lastName = lastName,
                number = number,
                userName = userName,
                latLng = GeoPoint(0.0,0.0)
            )
        )
        mDbApi.uploadProfileImage(profilePicture, id)
        return "Uspesno ste kreirali nalog!"
        //signOut() // za sada, testiram nesto
    }

    fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    suspend fun signOut(context: Context) {
        CurrentUserInfo.getInstance().user = null
        Firebase.auth.signOut()
        val packageName = context.packageName
        val runtime = Runtime.getRuntime()
        withContext(Dispatchers.IO) {
 //           runtime.exec("pm clear $packageName")
        }
    }
}