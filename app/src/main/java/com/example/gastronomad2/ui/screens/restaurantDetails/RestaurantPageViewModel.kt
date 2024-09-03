package com.example.gastronomad2.ui.screens.restaurantDetails

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gastronomad2.models.entities.Comment
import com.example.gastronomad2.models.entities.Restaurant
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.db.implementations.RestaurantDbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking

class RestaurantPageViewModel private constructor() : GastonomadAppViewModel(){

    companion object {
        private var INSTANCE: RestaurantPageViewModel? = null

        fun getInstance(): RestaurantPageViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RestaurantPageViewModel().also {
                    INSTANCE = it
                }
            }
        }
    }

    var currentRestaurantId: String? = null

    var restaurant by mutableStateOf(Restaurant())
        private set

    var restaurantPictures: MutableState<List<Uri>> = mutableStateOf(emptyList())
        private set
    var showRatingDialog by mutableStateOf(false)
    var ratingInput = MutableStateFlow("")


    var comments by mutableStateOf(listOf<Comment>())
    private set
    var showComments by mutableStateOf(true)
        private set

    var newCommentText by mutableStateOf("")

    fun showDialog() {
        showRatingDialog = true
    }
    fun hideDialog() {
        showRatingDialog = false
    }

    fun toggleCommentsVisibility() {
        showComments = !showComments
    }

    fun loadComments() {
        comments = DbApi().getCommentsForRestaurant(restaurant.id!!)
        toggleCommentsVisibility()
    }

    fun addComment() {
        var newComment = Comment( // Ovo će generisati Firebase
            restaurantId = restaurant.id,
            userId = Firebase.auth.currentUser!!.uid,
            text = newCommentText,
            username = CurrentUserInfo.getInstance().get()!!.userName
        )


        //SearchViewModel.getInstance().updateEvent(event)

        newComment.commentId = DbApi().addComment(restaurantId = restaurant.id!!, text = newCommentText,username = newComment.username )

        comments = comments + newComment
        // sada update idja !!!

        newCommentText = ""
    }

    fun deleteComment(commentId: String) {
        comments = comments.filter { it.commentId != commentId }
        //restaurant = restaurant.copy(commentNumber = restaurant.commentNumber!! - 1)

       // SearchViewModel.getInstance().updateEvent(event)

        runBlocking {
            DbApi().deleteComment(commentId)
        }
    }

    fun updateRestaurant(newRestaurant: Restaurant) {
        restaurant = newRestaurant
    }

    fun addRestaurantPicture(uri:Uri){
        this.restaurantPictures.value += uri
    }

     fun submitRating() {
         // Ovdje dodaj kod za slanje ocene na server
         //dbApi().submitRestaurantRating(restaurant.id!!, ratingInput)
         if (ratingInput.value != "") {
             DbApi().submitRestaurantRating(restaurant.id!!, ratingInput.value.toInt())
             // Nakon što se ocena pošalje, ažuriraj restoran i sakrij dijalog
             //restaurant = restaurant.copy(rating = RestaurantDbApi().get(restaurant.id!!).rating)
             hideDialog()
         }
     }

    fun updateNumberRating(newRating : String)
    {
        ratingInput.value = newRating
    }
}