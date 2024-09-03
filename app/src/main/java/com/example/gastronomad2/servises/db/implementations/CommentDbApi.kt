package com.example.gastronomad2.servises.db.implementations

import android.util.Log
import com.example.gastronomad2.models.entities.Comment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class CommentDbApi {

    suspend fun add(id: String? = null, comment: Comment): String {
        var result: Boolean = true
        var commentId: String = ""
        if (id == null) {
            val commentRef = Firebase.firestore.collection("comment").add(comment).await()
            commentId = commentRef.id
            Firebase.firestore.collection("comment").document(commentId)
                .update("commentId", commentId)
                .addOnFailureListener { ex ->
                    Log.d("COMMENT_DB_ADD_WITH_UPDATE", "UPDATE FAILED WITH ", ex)
                    result = false
                }

        } else {
            Firebase.firestore.collection("comments").document(id).set(comment)
                .addOnFailureListener { ex ->
                    Log.d("COMMENT_DB_ADD", "ADD FAILED WITH ", ex)
                    result = false
                }
        }

        return commentId
    }

    suspend fun get(id: String): Comment? {
        var comment: Comment? = null
        val doc = Firebase.firestore.collection("comment").document(id).get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    Log.d("COMMENT_DB_GET", "GET SUCCEEDED")
                }
                return@addOnSuccessListener
            }
            .addOnFailureListener { ex ->
                Log.d("COMMENT_DB_GET", "GET FAILED WITH ", ex)
            }
            .await()
        comment = doc.toObject<Comment>()
        return comment
    }

    suspend fun update(id: String, commentField: String, newValue: Any): Boolean {
        var result: Boolean = true
        Firebase.firestore.collection("comments").document(id).update(commentField, newValue)
            .addOnFailureListener { ex ->
                Log.d("COMMENT_DB_UPDATE", "UPDATE FAILED WITH ", ex)
                result = false
            }
        return result
    }

    suspend fun delete(id: String): Boolean {
        var result: Boolean = true
        Firebase.firestore.collection("comment").document(id).delete()
            .addOnFailureListener { ex ->
                Log.d("COMMENT_DB_DELETE", "DELETE FAILED WITH ", ex)
                result = false
            }
        return result
    }

    suspend fun getCommentsForRestaurant(restaurantId: String): List<Comment> {
        val querySnapshot = Firebase.firestore.collection("comment")
            .whereEqualTo("restaurantId", restaurantId)
            .get()
            .await()
        return querySnapshot.documents.mapNotNull {
            it.toObject<Comment>()
        }
    }
}