package com.example.crm_bueno2.utils


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val database: FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance("https://crmdb1-default-rtdb.europe-west1.firebasedatabase.app")
    }
}
