package com.example.gastronomad2.servises.db.implementations

import android.util.Log
import com.example.gastronomad2.models.entities.Restaurant
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kotlin.math.cos

class RestaurantDbApi {

    //dodavanje

    //dodavanje eventa
    suspend fun add(restaurant: Restaurant): String {

        var restaurantId = ""

        val restaurantRef = Firebase.firestore.collection("restaurant").add(restaurant).await()

        restaurantId = restaurantRef.id

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

    suspend fun giveAllNearbyRestaurants(lat: Double, lng: Double, radius: Double): List<Restaurant> {
        val radiusInKm = radius
        val earthRadiusKm = 6371.0

        val latDelta = Math.toDegrees(radiusInKm / earthRadiusKm)
        val lngDelta = Math.toDegrees(radiusInKm / (earthRadiusKm * cos(Math.toRadians(lat))))

        val minLat = lat - latDelta
        val maxLat = lat + latDelta
        val minLng = lng - lngDelta
        val maxLng = lng + lngDelta

        val nearRestaurants = mutableListOf<Restaurant>()

        try {
            // Awaiting the result from Firebase Firestore
            val querySnapshot = Firebase.firestore.collection("restaurant")
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val geoPoint = document.getGeoPoint("geoLocation")
                if (geoPoint != null) {
                    val pointLat = geoPoint.latitude
                    val pointLng = geoPoint.longitude
                    if (pointLat in minLat..maxLat && pointLng in minLng..maxLng) {
                        // Pretpostavljamo da su podaci u dokumentu ispravno mapirani na klasu Restaurant
                        val restaurant = document.toObject(Restaurant::class.java)
                        if (restaurant != null) {
                            nearRestaurants.add(restaurant)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Log or handle the exception as needed
            e.printStackTrace()
        }

        if(nearRestaurants.isNotEmpty())
        return nearRestaurants

        return emptyList()
    }


}