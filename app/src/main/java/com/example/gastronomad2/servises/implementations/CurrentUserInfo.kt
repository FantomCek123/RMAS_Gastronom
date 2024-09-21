package com.example.gastronomad2.servises.implementations

import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.db.DbApi
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CurrentUserInfo private constructor() {

    companion object {
        private var INSTANCE: CurrentUserInfo? = null

        fun getInstance(): CurrentUserInfo {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CurrentUserInfo().also {
                    INSTANCE = it
                }
            }
        }
    }

    var user : User? = null
    var signed = Firebase.auth.currentUser

    fun get() : User? {
        if(signed != null && user == null) {
            user = DbApi().getUser(signed!!.uid)!!
            return user
        }
        else if(Firebase.auth.currentUser == null)
            return null // guest user !
        return user
    }
}
