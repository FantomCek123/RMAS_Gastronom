package com.example.gastronomad2.models.entities

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

data class User(
    val id: String = "",

    val userName: String = "",
    val email: String = "",
    val firstName:String = "",
    val lastName:String = "",
    val number:String = "",
    val profilePicture:String = "",
    val latLng: GeoPoint? = null

// val password: String = " Ne treba najverovatnije
)