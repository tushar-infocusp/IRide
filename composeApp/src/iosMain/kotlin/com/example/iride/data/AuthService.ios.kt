package com.example.iride.data

import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIRPhoneAuthProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSError
import kotlin.coroutines.resume

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FirebaseOTPAuthManager : OTPAuthManager {

    private var verificationId: String? = null

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun sendOTP(phoneNumber: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->

            val phoneProvider = FIRPhoneAuthProvider.provider()

            phoneProvider.verifyPhoneNumber(
                phoneNumber,
                UIDelegate = null
            ) { verificationID, error ->

                if (error != null) {
                    cont.resume(Result.failure(Throwable(error.localizedDescription)))
                    return@verifyPhoneNumber
                }

                verificationId = verificationID
                cont.resume(Result.success(Unit))
            }
        }

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun verifyOTP(otp: String): Result<Boolean> =
        suspendCancellableCoroutine { cont ->

            val id = verificationId

            if (id == null) {
                cont.resume(Result.failure(Throwable("Verification ID is null")))
                return@suspendCancellableCoroutine
            }

            val credential = FIRPhoneAuthProvider.provider()
                .credentialWithVerificationID(id, verificationCode = otp)

            FIRAuth.auth().signInWithCredential(credential) { _, error ->

                if (error != null) {
                    cont.resume(Result.failure(Throwable(error.localizedDescription)))
                } else {
                    cont.resume(Result.success(true))
                }
            }
        }
}


actual class FirebaseEmailAuthManager : SignInAuthManager {

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun signUp(
        email: String,
        password: String
    ): Result<Boolean> = suspendCancellableCoroutine { cont ->

        FIRAuth.auth().createUserWithEmail(email, password) { _, error ->

            if (error != null) {

                val errorCode = (error as NSError).code

                // 🔥 If user already exists → try login
                if (errorCode == 17007L) { // ERROR_EMAIL_ALREADY_IN_USE

                    FIRAuth.auth().signInWithEmail(email = email,password = password) { _, loginError ->

                        if (loginError != null) {
                            cont.resume(Result.failure(Throwable(loginError.localizedDescription)))
                        } else {
                            cont.resume(Result.success(true))
                        }
                    }

                } else {
                    cont.resume(Result.failure(Throwable(error.localizedDescription)))
                }

            } else {
                // New user created
                cont.resume(Result.success(true))
            }
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun login(email: String, password: String): Result<Boolean> =
        suspendCancellableCoroutine { cont ->

            FIRAuth.auth().signInWithEmail(email = email,password = password) { _ , error ->
                if (error != null) {
                    cont.resume(Result.failure(Throwable(error.localizedDescription)))
                } else {
                    cont.resume(Result.success(true))
                }
            }
        }
}