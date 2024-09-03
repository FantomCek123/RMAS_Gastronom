package com.example.gastronomad2.servises.db.implementations

import android.net.Uri
import android.util.Log
import com.example.gastronomad2.ui.screens.profile.DrawProfilePageViewModel
import com.example.gastronomad2.ui.screens.restaurantDetails.RestaurantPageViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MediaDbApi {
    private val storageRef = Firebase.storage.reference
    private val users = "users/"
    private val profPic = "/profilePicture.jpg"


    fun uploadProfileImage(
        uri: Uri,
        uid: String,
    ) {

        val upr = storageRef.child(users + uid + profPic)

        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }

        GlobalScope.launch(Dispatchers.IO) {

            // Upload file and metadata to the path upr
            val uploadTask = upr.putFile(uri, metadata)

            uploadTask
                .addOnProgressListener {

                    val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                    Log.d("PICTURE_UPLOAD", "Upload is $progress% done")

                }.addOnPausedListener {
                    Log.d("PICTURE_UPLOAD", "Upload is paused")

                }.addOnFailureListener {
                    // Handle unsuccessful uploads

                }.addOnSuccessListener {
                    // Handle successful uploads on complete
                    // ...
                }
        }
    }

    fun downloadProfilePicture(
        uid: String,
    )  {
        val profileViewModel: DrawProfilePageViewModel = DrawProfilePageViewModel.getInstance()

        val upr = storageRef.child(users + uid + profPic)

        upr.downloadUrl
            .addOnSuccessListener {
                profileViewModel.setProfilePicture(it)
            }.addOnFailureListener {
                Log.e("PROFILE_PIC_DOWNLOAD", it.message ?: "Nema poruke o gresci")
            }

    }

    fun uploadRestaurantImages(
        uris: List<Uri>,
        uid: String,
        rid: String,
    ) {



        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }
        var picNum = 1
        uris.forEach { uri ->

            val ep = storageRef.child("$users$uid/$rid/$picNum")
            picNum++
            GlobalScope.launch(Dispatchers.IO) {

                // Upload file and metadata to the path upr
                val uploadTask = ep.putFile(uri, metadata)

                uploadTask
                    .addOnProgressListener {
                        //val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
                        //Log.d("PICTURE_UPLOAD", "Upload is $progress% done")
                    }.addOnPausedListener {
                        Log.d("PICTURE_UPLOAD", "Upload is paused")

                    }.addOnFailureListener {
                        // Handle unsuccessful uploads

                    }.addOnSuccessListener {
                        // Handle successful uploads on complete
                        // ...
                    }
            }
        }

    }
    fun downloadRestaurantPictures(
        uid: String,
        rid: String,
    ) {
        val rvm = RestaurantPageViewModel.getInstance()
        val restaurantRef = storageRef.child("$users$uid/$rid")

        GlobalScope.launch(Dispatchers.IO) {

            restaurantRef.listAll()
                .addOnSuccessListener { list ->

                    Log.e("EVENT_PIC_DOWNLOAD", list.items.toString() + "    ovo je lista")
                    list.items.forEach { ref ->

                        ref.downloadUrl
                            .addOnSuccessListener {
                                rvm.addRestaurantPicture(it)

                            }.addOnFailureListener {
                                Log.e("PROFILE_PIC_DOWNLOAD", it.message ?: "Nema poruke o gresci")
                            }
                    }
                }.addOnFailureListener {
                    Log.e("PROFILE_PIC_DOWNLOAD", it.message ?: "Nema poruke o gresci")
                }
        }
    }
}