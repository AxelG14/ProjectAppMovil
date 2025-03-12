package com.example.projectappmovil.controller

import androidx.navigation.NavController
import com.example.projectappmovil.navegation.AppScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginController {
    fun iniciarSesion(email: String, password: String, navController: NavController) {
        val auth: FirebaseAuth = Firebase.auth
        val db = Firebase.firestore

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userUid = auth.currentUser?.uid

                    if (userUid != null) {
                        db.collection("usuarios")
                            .document(userUid)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val rol = documentSnapshot.getString("rol")

                                    when (rol) {
                                        "admin" -> navController.navigate(route = AppScreens.InicioAdminScreen.route)
                                        else -> navController.navigate(route = AppScreens.InicioScreen.route)
                                    }
                                } else {
                                    println("El usuario no existe en Firestore")
                                }
                            }
                            .addOnFailureListener { exception ->
                                println("Error al obtener el documento del usuario: ${exception.message}")
                            }
                    } else {
                        println("No se pudo obtener el UID del usuario")
                    }
                } else {
                    println("Error en el inicio de sesión: ${task.exception?.message}")
                }
            }
    }
}