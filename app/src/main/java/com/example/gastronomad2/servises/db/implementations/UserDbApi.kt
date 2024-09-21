package com.example.gastronomad2.servises.db.implementations

import android.util.Log
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.screens.myRestaurants.MyRestaurantsViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDbApi {
    private val userRoot = Firebase.firestore.collection("user")

    fun updateUserLocation(uid: String,location: GeoPoint) {
        userRoot.document(uid).update("latLng",location). //await()
        addOnSuccessListener {
            Log.d("UPDATED_USER_LOCATION","Successfully updated user location.")
        }.
        addOnFailureListener(){
            Log.d("FAILED_TO_UPDATE_USER_LOCATION", "Location update failed:\n$it")
        }.
        addOnCanceledListener {
            Log.d("UPDATE_USER_LOCATION_CANCELED", "Location update canceled.")
        }
    }

    suspend fun add(uid: String?, user: User) : Boolean {

        var success : Boolean = false
        if (uid == null)
            userRoot.add(user)
                .addOnSuccessListener {
                    Log.d("USER_DB_ADD", "User document created successfully!")
                    success = true
                }
                .addOnFailureListener { e -> Log.w("USER_DB_ADD", "Error creating user document!", e) }
        else{
            userRoot.document(uid).set(user)
                .addOnSuccessListener {
                    Log.d("USER_DB_ADD", "User document: $uid created successfully!")
                    success = true
                }
                .addOnFailureListener { e -> Log.w("USER_DB_ADD", "Error creating user document: $uid!", e) }
        }
        return success
    }

    suspend fun get(uid: String) : User? {
        var user: User? = null
        val ud = userRoot.document(uid).get()
            .addOnSuccessListener { document ->

                if (document != null) {
                    Log.d("USER_DB_GET", "DocumentSnapshot data: ${document.data}")

                } else {
                    Log.d("USER_DB_GET", "No such document!")
                }

                return@addOnSuccessListener
            }
            .addOnFailureListener { exception ->
                Log.w("USER_DB_GET", "Get user failed with ==> ", exception)
            }.await()
        user = ud.toObject<User>()
        return user
    }

     fun update(id: String, userField: String, newValue: Any): Boolean {
        var result: Boolean = true
        com.google.firebase.Firebase.firestore.collection("user").document(id).update(userField, newValue)
            .addOnFailureListener { ex ->
                Log.d("USER_DB_DELETE", "DELETE FAILED WITH ", ex)
                result = false
            }
        return result
    }

    suspend fun getUserWithUsername2(username:String):List<User> {
        val querySnapshot = userRoot
            .whereEqualTo("userName", username)
            .get()
            .await()
        return querySnapshot.documents.mapNotNull { it.toObject<User>() }
    }



    suspend fun getUserWithEmail(email:String):List<User> {
        val querySnapshot = userRoot
            .whereEqualTo("email", email)
            .get()
            .await()
        return querySnapshot.documents.mapNotNull { it.toObject<User>() }


    }

     fun updateLocation() : Boolean{
        var success : Boolean = false
        userRoot.document(CurrentUserInfo.getInstance().get()!!.id).update("latLng", LatLng(1.0, 1.0))
            .addOnSuccessListener {
                Log.d("USER_DB_UPDATE", "User document: ${CurrentUserInfo.getInstance().get()!!.id} updated successfully!")
                success = true
            }
            .addOnFailureListener { e -> Log.w("USER_DB_UPDATE", "Error creating user document: ${CurrentUserInfo.getInstance().get()!!.id}!", e) }
        return success
    }

    fun getRestaurantsForUser() {
        val db = Firebase.firestore

        GlobalScope.launch(Dispatchers.IO)
        {
            val querySnapshot = db.collection("restaurant")
                .whereEqualTo("publisherId", CurrentUserInfo.getInstance().get()!!.id)
                .get()

                .addOnSuccessListener {qs ->
                    MyRestaurantsViewModel.getInstance().setRestaurants(qs.documents.mapNotNull { it.toObject(Restaurant::class.java) })
                }.addOnFailureListener {
                    Log.e("RESTORANI_DOWNLOAD", it.message ?: "Nema poruke o gresci")
                }
        }
    }

    suspend fun getTopUsersByPoints(limit: Int = 20): List<User> {
        return try {
            val querySnapshot = userRoot.get().await()
            val users = querySnapshot.documents.mapNotNull { it.toObject<User>() }

            val sortedUsers = users.sortedByDescending { it.points }

            sortedUsers.take(limit)
        } catch (e: Exception) {
            emptyList() // Vraća praznu listu u slučaju greške
        }
    }
}