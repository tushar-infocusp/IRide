package com.example.iride.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.data.CountryData
import com.example.iride.data.OTPAuthManager
import com.example.iride.data.showToast
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.app_name
import com.example.iride.generated.resources.did_not_receive_a_code
import com.example.iride.generated.resources.enter_mobile_number
import com.example.iride.generated.resources.ic_back_arrow
import com.example.iride.generated.resources.mobile_number
import com.example.iride.generated.resources.resend_Code
import com.example.iride.generated.resources.send_otp
import com.example.iride.generated.resources.sign_in_to_your_sustainable_commute
import com.example.iride.generated.resources.verification_code
import com.example.iride.generated.resources.verify_and_continue
import com.example.iride.generated.resources.welcome_back
import com.example.iride.theme.darkBlue
import com.example.iride.theme.deepGreen
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.greyBlue
import com.example.iride.theme.mutedGreen
import com.example.iride.theme.primaryBackground
import com.example.iride.ui.common.CountryCodeDropdown
import com.example.iride.ui.common.OtpInputField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun PhoneVerificationScreen(
    onVerificationSuccess: () -> Unit,
    onBackClick : () -> Unit
) {

    val otpAuthManager : OTPAuthManager = koinInject()

    var phoneNumber by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            PhoneVerificationTopBar{
                onBackClick()
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .background(primaryBackground)
                .padding(start = 20.dp, end = 20.dp, top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = stringResource(Res.string.welcome_back),
                fontSize = 40.sp,
                fontWeight = FontWeight.W700,
                color = darkBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.sign_in_to_your_sustainable_commute),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = mutedGreen
            )

            Spacer(modifier = Modifier.height(60.dp))

            OTPVerificationCard(
                phoneNumber = phoneNumber,
                onPhoneNumberChange = {
                    phoneNumber = it
                },
                onVerificationSuccess = {
                    onVerificationSuccess()
                },
                sendOtp = {
                    val result = otpAuthManager.sendOTP(it)
                    result.isSuccess
                }
            ){
                val result = otpAuthManager.verifyOTP(it)
                result.isSuccess
            }
        }
    }
}

@Composable
fun OTPVerificationCard(
    phoneNumber: String,
    onVerificationSuccess : () -> Unit,
    sendOtp : suspend (String) -> Boolean,
    onPhoneNumberChange : (String) -> Unit,
    verifyOtp: suspend (String) -> Boolean
){

    val isEnabled = phoneNumber.length == 10
    val scope = rememberCoroutineScope()
    var sendingOTP by remember { mutableStateOf(false) }
    var selectedCountryData by remember { mutableStateOf(getCountryList().first())}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = stringResource(Res.string.mobile_number),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W600,
                    color = deepGreen
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    CountryCodeDropdown(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .wrapContentHeight(),
                        countries = getCountryList()
                    ){
                        selectedCountryData = it
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    EnterMobileNumberField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f),
                        phoneNumber = phoneNumber
                    ){
                        onPhoneNumberChange(it)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {

                    if(!sendingOTP){
                        Text(
                            text = stringResource(Res.string.send_otp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                            color = if (isEnabled) Color(0xFF006C49) else mutedGreen,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = isEnabled) {
                                    scope.launch {
                                        sendingOTP = true
                                        sendOtp(selectedCountryData.code.trim() + phoneNumber)
                                        sendingOTP = false
                                    }

                                }
                                .border(
                                    1.dp, if (isEnabled) {
                                        Color(0xFF006C49).copy(0.2f)
                                    } else mutedGreen.copy(0.2f), RoundedCornerShape(8.dp)
                                )
                                .background(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isEnabled) {
                                        Color(0xFF006C49).copy(0.1f)
                                    } else mutedGreen.copy(0.1f)
                                )
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }

                    else{
                        CircularProgressIndicator(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    Color(0xFF006C49).copy(0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                                .background(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF006C49).copy(0.1f)
                                )
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                                .size(16.dp),
                            color = Color(0xFF006C49),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            VerificationCodeCard(
                phoneNumber = phoneNumber,
                onVerificationSuccess = {
                    onVerificationSuccess()
                },
                sendOtp = {
                    sendOtp(it)
                },
                verifyOtp = verifyOtp
            )

        }
    }
}



@Composable
fun PhoneVerificationTopBar(
    onBackClick : () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .statusBarsPadding()
            .padding(bottom = 22.dp, start = 22.dp, end = 22.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(Res.drawable.ic_back_arrow),
            contentDescription = "I Ride Icon",
            modifier = Modifier
                .size(16.dp)
                .clickable{
                    onBackClick()
                },
            tint = emeraldGreen
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = stringResource(Res.string.app_name),
            fontSize = 20.sp,
            fontWeight = FontWeight.W700,
            color = emeraldGreen
        )
    }
}

@Composable
fun EnterMobileNumberField(
    modifier: Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
){

    OutlinedTextField(
        value = phoneNumber,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        onValueChange = {
            if(it.length <= 10){
                onPhoneNumberChange(it)
            }
        },
        label = null,
        placeholder = {
            Text(
                text =  stringResource(Res.string.enter_mobile_number),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color(0xFFF6B7280)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = primaryBackground,
            unfocusedContainerColor = primaryBackground,
            unfocusedBorderColor = Color(0XFFBFC9C3),
            focusedBorderColor = emeraldGreen
        )
    )
}

@Composable
fun VerificationCodeCard(
    phoneNumber: String,
    sendOtp : suspend (String) -> Unit,
    onVerificationSuccess : () -> Unit,
    verifyOtp : suspend (String) -> Boolean
){

    var verificationCode by remember { mutableStateOf("") }
    var secondsRemaining by remember { mutableStateOf(0) }
    var otpVerified by remember { mutableStateOf(false) }
    var verifyingOtp by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = secondsRemaining) {
        if (secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        }
    }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timerText = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(Res.string.verification_code),
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = deepGreen
        )

        if(secondsRemaining != 0){
            Text(
                text = timerText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF006C49)
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    OtpInputField(
        otpLength = 6,
        onOtpTextChange = {
            verificationCode = it
        }
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = {
            scope.launch {
                verifyingOtp = true
                otpVerified = verifyOtp(verificationCode)
                if(otpVerified){
                    showToast("Otp has been verified successfully")
                    onVerificationSuccess()
                }
                else{
                    showToast("Otp verification failed")
                }
                verifyingOtp = false
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = deepGreen
        ),
        shape = RoundedCornerShape(12.dp),
        enabled = verificationCode.length == 6 && !verifyingOtp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ){
        if(!verifyingOtp){
            Text(
                text = stringResource(Res.string.verify_and_continue),
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = Color.White
            )
        }
        else{
            CircularProgressIndicator(
                modifier = Modifier
                    .size(14.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(Res.string.did_not_receive_a_code),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            color = mutedGreen
        )

        Text(
            text = stringResource(Res.string.resend_Code),
            fontSize = 14.sp,
            fontWeight = FontWeight.W600,
            color = if(secondsRemaining == 0) Color(0xFF006C49) else greyBlue,
            modifier = Modifier
                .clickable(phoneNumber.length == 10 && secondsRemaining == 0){
                    scope.launch{
                        secondsRemaining = 60
                        sendOtp(phoneNumber)
                    }
                }
        )
    }
}

fun getCountryList() : List<CountryData>{
    val countries = listOf(
        CountryData("India", "+91", "IN", "🇮🇳"),
        CountryData("United States", "+1", "US", "🇺🇸"),
        CountryData("United Kingdom", "+44", "GB", "🇬🇧"),
        CountryData("Canada", "+1", "CA", "🇨🇦"),
        CountryData("Australia", "+61", "AU", "🇦🇺"),
        CountryData("Germany", "+49", "DE", "🇩🇪"),
        CountryData("France", "+33", "FR", "🇫🇷"),
        CountryData("Brazil", "+55", "BR", "🇧🇷"),
        CountryData("China", "+86", "CN", "🇨🇳"),
        CountryData("Japan", "+81", "JP", "🇯🇵"),
        CountryData("South Korea", "+82", "KR", "🇰🇷"),
        CountryData("UAE", "+971", "AE", "🇦🇪"),
        CountryData("Saudi Arabia", "+966", "SA", "🇸🇦"),
        CountryData("Singapore", "+65", "SG", "🇸🇬"),
        CountryData("South Africa", "+27", "ZA", "🇿🇦"),
        CountryData("Russia", "+7", "RU", "🇷🇺"),
        CountryData("Italy", "+39", "IT", "🇮🇹"),
        CountryData("Spain", "+34", "ES", "🇪🇸"),
        CountryData("Netherlands", "+31", "NL", "🇳🇱"),
        CountryData("Turkey", "+90", "TR", "🇹🇷")
    )
    return countries
}
