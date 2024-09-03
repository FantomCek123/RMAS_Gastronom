package com.example.gastronomad2.models.entities

import android.media.Image
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.sql.Time
import java.util.Date

data class Restaurant (

    val id: String? = "",
    val publisherId: String? = "",

    val title: String? = "",  ////////////
    val location: String? = "", // mozda, a mozda i ne ce vidimo kolko me ne mrzi
    val description: String? = "",
    val image: Image? = null /*TODO utvrdi sta treba ICON ili IMAGE tip*/, //ussles???
    var rating:Double = 0.0, ////////////
    var numOfRates:Int = 0,
    val geoLocation: GeoPoint? = null,
    val date: Timestamp = Timestamp.now(), ////////////
    val type: String? = "" ////////////
    )