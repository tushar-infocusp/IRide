package com.example.iride.data

expect class FirebaseOTPAuthManager(): OTPAuthManager {
    override suspend fun sendOTP(phoneNumber: String): Result<Unit>
    override suspend fun verifyOTP(otp: String): Result<Boolean>
}

interface OTPAuthManager {
    suspend fun sendOTP(phoneNumber: String): Result<Unit>
    suspend fun verifyOTP(otp: String): Result<Boolean>
}

interface SignInAuthManager {
    suspend fun signUp(email: String, password: String): Result<Boolean>
    suspend fun login(email: String, password: String): Result<Boolean>
}

sealed class OTPResult {
    object Success : OTPResult()
    data class Error(val message: String) : OTPResult()
}

expect class FirebaseEmailAuthManager() : SignInAuthManager {
    override suspend fun signUp(
        email: String,
        password: String
    ): Result<Boolean>

    override suspend fun login(
        email: String,
        password: String
    ): Result<Boolean>


}