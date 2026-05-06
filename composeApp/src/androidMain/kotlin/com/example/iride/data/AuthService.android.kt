package com.example.iride.data

import android.app.Activity
import android.content.Context
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FirebaseOTPAuthManager : OTPAuthManager {

    private var verificationId: String? = null
    private var activity: Activity? = null

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    actual override suspend fun sendOTP(phoneNumber: String): Result<Unit> {
        return suspendCancellableCoroutine { cont ->

            val currentActivity = activity
            if (currentActivity == null) {
                cont.resume(Result.failure(Exception("Activity is not set for Firebase Phone Auth")))
                return@suspendCancellableCoroutine
            }

            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(currentActivity)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        cont.resume(Result.success(Unit))
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        cont.resume(Result.failure(e))
                    }

                    override fun onCodeSent(
                        verId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        verificationId = verId
                        cont.resume(Result.success(Unit))
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    actual override suspend fun verifyOTP(otp: String): Result<Boolean> {
        return try {
            val credential = PhoneAuthProvider.getCredential(
                verificationId ?: return Result.failure(Exception("No verification ID")),
                otp
            )

            FirebaseAuth.getInstance().signInWithCredential(credential).await()

            Result.success(true)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FirebaseEmailAuthManager actual constructor() :
    SignInAuthManager {
    actual override suspend fun signUp(
        email: String,
        password: String
    ): Result<Boolean> {
        return Result.failure(Exception("Not implemented"))
    }

    actual override suspend fun login(
        email: String,
        password: String
    ): Result<Boolean> {
        return Result.failure(Exception("Not implemented"))
    }
}