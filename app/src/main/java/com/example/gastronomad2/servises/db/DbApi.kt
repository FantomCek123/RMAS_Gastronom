package com.example.gastronomad2.servises.db

import android.media.Image
import android.util.Log
import com.example.gastronomad2.models.entities.Comment
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.models.entities.User
import com.example.gastronomad2.servises.db.implementations.CommentDbApi
import com.example.gastronomad2.servises.db.implementations.MediaDbApi
import com.example.gastronomad2.servises.db.implementations.RestaurantDbApi
import com.example.gastronomad2.servises.db.implementations.UserDbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.google.firebase.Firebase
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await


// DODATI DODATNI DbApi-i i KODZA FUNKCIJE
class DbApi {

    private val users: UserDbApi = UserDbApi()
    private val mdbApi : MediaDbApi = MediaDbApi()
    private val rdbApi : RestaurantDbApi = RestaurantDbApi()
    private val cdbApi: CommentDbApi = CommentDbApi()



    fun addUser(user: User) {
        runBlocking {
            users.add(user.id, user)
        }
    }

    fun getUser(id:String): User?{

        Log.d("getUser_DbApi", "User ID is: $id")

        var u:User? = null
        runBlocking {
            launch {

                val ua = async {users.get(id) }
                u = ua.await()
            }
        }

        return u
    }

    fun getUserWithUsername(username: String): Boolean {
        var u: List<User>? = null
        runBlocking {
            u = async { users.getUserWithUsername2(username) }.await()
        }
        return u!!.isNotEmpty()
    }

    fun getUserWithEmail(email: String): User {
        var u: List<User>? = null
        runBlocking {
            u = async { users.getUserWithEmail(email) }.await()
        }
        return u!!.firstOrNull()!!
    }


    fun updateUserLocation(uid: String,location: GeoPoint)
    {
        users.updateUserLocation(uid,location)
    }

    fun downloadProfilePicture(
        uid: String,
    ) {
        mdbApi.downloadProfilePicture(uid)
    }

    fun addRestaurant(
        publisherId: String = "",
        title: String = "",
        location: String = "",
        description: String = "",
        image: Image? = null,
        lat: Double,
        lng: Double,
        type: String,
    ) : String {

        var id: String

        val restaurant: Restaurant = Restaurant(
            publisherId = publisherId,
            title = title,
            location = location,
            description = description,
            image = image,
            geoLocation = GeoPoint(lat, lng),
            type = type
        )

        runBlocking {
            id = async { this@DbApi.rdbApi.add(restaurant) }.await()
        }

        users.update(publisherId,"points",CurrentUserInfo.getInstance().get()!!.points + 20)
        return id
    }

    fun deleteRestaurant(id:String) {
        GlobalScope.launch {
            rdbApi.delete(id)
        }
    }

    fun getComment(id:String):Comment? {
        var comment: Comment? = null
        runBlocking {
            comment = async { this@DbApi.cdbApi.get(id) }.await()
        }
        return comment
    }

    fun getCommentsForRestaurant(restaurantId:String):List<Comment>
    {
        var cs: List<Comment> = listOf(Comment())
        runBlocking{
            cs = async { cdbApi.getCommentsForRestaurant(restaurantId) }.await()
        }
        return cs
    }

    fun updateComment(id: String, commentField: String, newValue: Any) {
        GlobalScope.launch {
            cdbApi.update(id,commentField,newValue)
        }
    }

    fun updateUser(id: String, userField: String, newValue: Any) {
        GlobalScope.launch {
            users.update(id,userField,newValue)
        }
    }

    fun addComment(id: String? = null, restaurantId: String = "", title: String = "", text: String = "",username: String = ""
    ) : String {
        var newCommentId : String = ""
        val comment: Comment = Comment(
            commentId = id,
            restaurantId = restaurantId,
            userId = CurrentUserInfo.getInstance().get()!!.id,
            text =  text,
            username = username
        )

        runBlocking {
            newCommentId = cdbApi.add(id, comment)
            }
        users.update(CurrentUserInfo.getInstance().get()!!.id,"points",CurrentUserInfo.getInstance().get()!!.points + 1)

        return newCommentId
    }

    fun deleteComment(id:String) {
        runBlocking {
            cdbApi.delete(id)
        }
    }

     fun submitRestaurantRating(restaurantId: String, ratingInput: Int) {
        GlobalScope.launch {
            val restaurant = rdbApi.get(restaurantId)

            val newRating: Double =
                (restaurant!!.rating * restaurant.numOfRates + ratingInput) / (restaurant.numOfRates + 1)

            rdbApi.update(restaurantId, "numOfRates", restaurant!!.numOfRates + 1)
            rdbApi.update(restaurantId, "rating", newRating)
            users.update(CurrentUserInfo.getInstance().get()!!.id,"points",CurrentUserInfo.getInstance().get()!!.points + 5)
        }
    }
    fun getRestaurantsWithTitle(title:String?):List<Restaurant?>
    {
        var evs: List<Restaurant?> = emptyList()
        runBlocking{
            evs = async { rdbApi.findRestaurantsWhitTitles(title) }.await()
        }
        return evs
    }

    fun getRestaurantsWithRating(rating:Double?):List<Restaurant?>
    {
        var evs: List<Restaurant?> = emptyList()
        runBlocking{
            evs = async { rdbApi.findRestaurantsWhitRating(rating) }.await()
        }
        return evs
    }

    fun getRestaurantsWithTypes(types:List<String>?):List<Restaurant?>
    {
        var evs: List<Restaurant?> = emptyList()
        runBlocking{
            evs = async { rdbApi.findRestaurantsWhitTypes(types) }.await()
        }
        return evs
    }

    fun getTopUsersByPoints() : List<User?>
    {
        var usersList: List<User?> = emptyList()
        runBlocking{
            usersList = async { users.getTopUsersByPoints() }.await()
        }
        return usersList
    }

    fun getNearbyRestaurants(lat:Double, lng:Double, redius:Double) : List<Restaurant> {
        var restaurantList: List<Restaurant>
        runBlocking {
            restaurantList = rdbApi.giveAllNearbyRestaurants(lat, lng, redius)
        }
        return restaurantList
    }

}