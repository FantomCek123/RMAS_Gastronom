package com.example.gastronomad2.servises.db.implementations

import android.util.Log
import com.example.gastronomad2.models.entities.Restaurant
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class RestaurantDbApi {

    //dodavanje

    //dodavanje eventa
    suspend fun add(restaurant: Restaurant): String {

        var restaurantId = ""

        val eventRef = Firebase.firestore.collection("restaurant").add(restaurant).await()

        restaurantId = eventRef.id

        Firebase.firestore.collection("restaurant")
            .document(restaurantId)
            .update("id", restaurantId)
            .addOnFailureListener { ex ->
                Log.d("restaurant_DB_ADD_WITH_UPDATE", "UPDATE FAILED WITH ", ex)

            }

        return restaurantId
    }

    //vraca event sa za prosledjenim idjem
    suspend fun get(id: String): Restaurant? {
        var restaurant: Restaurant? = null
        val doc = Firebase.firestore.collection("restaurant").document(id).get()
            /*.addOnSuccessListener { doc ->
                if (doc != null) {
                    Log.d("EVENT_DB_GET", "GET SUCCEDED")
                }

                return@addOnSuccessListener
            }*/
            .addOnFailureListener { ex ->
                Log.d("restaurant_DB_GET", "GET FAILED WITH ", ex)
            }
            .await()
        restaurant = doc.toObject<Restaurant>()
        return restaurant
    }

    //updatuje event sa prosledjenim idjem
    suspend fun update(id: String, restaurantField: String, newValue: Any): Boolean {
        var result: Boolean = true
        Firebase.firestore.collection("restaurant").document(id).update(restaurantField, newValue)
            .addOnFailureListener { ex ->
                Log.d("restaurant_DB_DELETE", "DELETE FAILED WITH ", ex)
                result = false
            }
        return result
    }

    //brise event sa odredjenim idjem

    suspend fun delete(id: String): Boolean {
        var result: Boolean = true
        Firebase.firestore.collection("restaurant").document(id).delete()
            .addOnFailureListener { ex ->
                Log.d("restaurant_DB_DELETE", "DELETE FAILED WITH ", ex)
                result = false
            }
        return result
    }

    suspend fun findRestaurantsWhitTitles(title: String?): List<Restaurant?> {

        if (!title.isNullOrEmpty()) {
            val querySnapshot = Firebase.firestore.collection("restaurant")
                .whereEqualTo("title", title)//treba type
                //.whereEqualTo("rating", 6)
                .get()
                .await()
            return querySnapshot.documents.mapNotNull { it.toObject<Restaurant>() }
        } else { // ovo ni ne mora ali neka ga za sad cisto da ne zove bazu ako se zna da nista nece vrati

            return emptyList()
        }
    }

    suspend fun findRestaurantsWhitRating(rating: Double?): List<Restaurant?> {

        if (rating != null && rating > 0.0 && rating < 10.0) {
            val querySnapshot = Firebase.firestore.collection("restaurant")
                .whereGreaterThanOrEqualTo("rating", rating)
                //.whereGreaterOThan("rating", rating)
                .get()
                .await()
            return querySnapshot.documents.mapNotNull { it.toObject<Restaurant>() }
        } else { // ovo ni ne mora ali neka ga za sad cisto da ne zove bazu ako se zna da nista nece vrati

            return emptyList()
        }
    }

    suspend fun findRestaurantsWhitTypes(types: List<String?>?): List<Restaurant?> {

        if (!types.isNullOrEmpty()) {
            val querySnapshot = Firebase.firestore.collection("restaurant")
                .whereIn("type", types)//treba type
                .get()
                .await()
            return querySnapshot.documents.mapNotNull { it.toObject<Restaurant>() }
        } else { // ovo ni ne mora ali neka ga za sad cisto da ne zove bazu ako se zna da nista nece vrati

            return emptyList()
        }
    }



}