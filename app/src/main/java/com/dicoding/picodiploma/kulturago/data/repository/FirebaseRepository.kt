package com.dicoding.picodiploma.kulturago.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.auth0.jwt.JWT
import com.dicoding.picodiploma.kulturago.BuildConfig
import com.auth0.jwt.algorithms.Algorithm
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseRepository(private val context: Context) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("KulturaGoPrefs", Context.MODE_PRIVATE)

    private val jwtSecret: String = BuildConfig.JWT_SECRET_KEY

    suspend fun login(email: String, password: String): String {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        val token = generateJwtToken(currentUser?.uid ?: "")
                        saveJwtToken(token)
                        continuation.resume("Login Successful")
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Login Failed")
                        )
                    }
                }
        }
    }

    suspend fun register(fullName: String, email: String, password: String): String {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName)
                                .build()
                        )
                        val token = generateJwtToken(user?.uid ?: "")
                        saveJwtToken(token)
                        continuation.resume("Registration Successful")
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Registration Failed")
                        )
                    }
                }
        }
    }

    private fun generateJwtToken(userId: String): String {
        val algorithm = Algorithm.HMAC256(jwtSecret)
        return JWT.create()
            .withIssuer("KulturaGo")
            .withClaim("userId", userId)
            .withIssuedAt(java.util.Date())
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day expiration
            .sign(algorithm)
    }

    private fun saveJwtToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun getJwtToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun logout() {
        auth.signOut()
        sharedPreferences.edit().remove("jwt_token").apply()
    }


    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}

