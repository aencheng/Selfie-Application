package com.example.project9

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects

class ImageViewModel : ViewModel(){
    private var auth: FirebaseAuth = Firebase.auth
    private val tag = "ImagesViewModel"
    var user: User? = null
    var verifyPassword = ""
    private val _images: MutableLiveData<MutableList<Image>> = MutableLiveData()
    val images: LiveData<List<Image>>
        get() = _images as LiveData<List<Image>>

    init {
        initializeStorage()
        _images.value = mutableListOf()
    }

    fun initializeStorage() {
        val firestoreDB = FirebaseFirestore.getInstance()
        val currentUser = getCurrentUser()

        currentUser?.uid?.let { userId ->
            // Directly query the "images" subcollection based on the UID
            val imagesReference = firestoreDB
                .collection("images")
                .document(userId) // Document ID is the user's UID
                .collection("user_images")
                .limit(30)
                .orderBy("creation_time_ms", Query.Direction.DESCENDING)

            imagesReference.addSnapshotListener { snapshot, exception ->
                if (exception != null || snapshot == null) {
                    Log.e(tag, "Exception when querying images", exception)
                    return@addSnapshotListener
                }

                val imageList = snapshot.toObjects<Image>()
                _images.value = imageList.toMutableList()

                for (image in imageList) {
                    Log.i(tag, "Image $image")
                }
            }
        }
    }

    fun clearImages() {
        _images.value = mutableListOf()
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        user = null
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}