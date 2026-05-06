package com.example.iride.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.data.OTPAuthManager
import com.example.iride.data.SignInAuthManager
import com.example.iride.data.showToast
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.access_your_corporate_sustainable_transit_hub
import com.example.iride.generated.resources.app_name
import com.example.iride.generated.resources.email_address
import com.example.iride.generated.resources.forgot_password
import com.example.iride.generated.resources.ic_iride
import com.example.iride.generated.resources.ic_leaf
import com.example.iride.generated.resources.ic_lock
import com.example.iride.generated.resources.ic_message
import com.example.iride.generated.resources.password
import com.example.iride.generated.resources.secure_sso_integration_for_authorized_employees_only
import com.example.iride.generated.resources.sign_in
import com.example.iride.generated.resources.welcome_back_eco_warrior
import com.example.iride.theme.darkBlue
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.mutedGreen
import com.example.iride.theme.pastelBlue
import com.example.iride.util.isValidEmail
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.math.sign

@Composable
fun SignInScreen(
    openHomeScreen : () -> Unit,
    onBackClick : () -> Unit
){

    val scrollState = rememberScrollState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val signInAuthManager : SignInAuthManager = koinInject()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            PhoneVerificationTopBar{
                onBackClick()
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(pastelBlue)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            Card(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_leaf),
                        contentDescription = "Leaf",
                        modifier = Modifier
                            .size(28.dp),
                        tint = emeraldGreen
                    )

                    Text(
                        text = stringResource(Res.string.app_name),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        color = emeraldGreen
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(Res.string.welcome_back_eco_warrior),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W600,
                        color = darkBlue,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(Res.string.access_your_corporate_sustainable_transit_hub),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400,
                        color = mutedGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(47.dp))

                    EmailAndPasswordCard(
                        email = email,
                        password = password,
                        onEmailChange = {
                            email = it
                        },
                        onPasswordChange = {
                            password = it
                        },
                        signIn = { email , password ->
                            val result = signInAuthManager.signUp(email,password)
                            result.isSuccess
                        }
                    ){
                        openHomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun EmailAndPasswordCard(
    email : String,
    password : String,
    onEmailChange : (String) -> Unit,
    onPasswordChange : (String) -> Unit,
    signIn : suspend (String, String) -> Boolean,
    openHomeScreen : () -> Unit
){

    var signingIn by remember { mutableStateOf(false) }
    val isValidEmail by derivedStateOf {
        email.isValidEmail()
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(Res.string.email_address),
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = mutedGreen
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = email,
            onValueChange = {
                onEmailChange(it)
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFBFC9C3),
                focusedBorderColor = mutedGreen,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_message),
                    contentDescription = "Message Icon",
                    modifier = Modifier
                        .width(20.dp)
                        .height(16.dp),
                    tint = Color(0xFF707974)
                )
            },
            placeholder = {
                Text(
                    text = "name@company.com",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF707974)
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(Res.string.password),
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = mutedGreen
        )
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = password,
            onValueChange = {
                onPasswordChange(it)
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFBFC9C3),
                focusedBorderColor = mutedGreen,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_lock),
                    contentDescription = "Message Icon",
                    modifier = Modifier
                        .width(16.dp)
                        .height(21.dp),
                    tint = Color(0xFF707974)
                )
            },
            placeholder = {
                Text(
                    text = "Enter Password",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFF707974)
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            border = BorderStroke(width = 2.dp, color = emeraldGreen),
            onClick = {
                scope.launch {
                    signingIn = true

                    if(signIn(email, password)){
                        showToast("Email verification successful")
                        openHomeScreen()
                    }
                    else{
                        showToast("Email verification failed")
                    }

                    signingIn = false
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = CircleShape,
            enabled = (isValidEmail && password.length >= 6) && !signingIn
        ){
            if(!signingIn){
                Text(
                    text = stringResource(Res.string.sign_in),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = emeraldGreen
                )
            }
            else{
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(16.dp),
                    color = emeraldGreen,
                    strokeWidth = 2.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color(0xFFBFC9C3))

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ){
            Icon(
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = "Lock Icon",
                modifier = Modifier
                    .width(10.dp)
                    .height(12.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                stringResource(Res.string.secure_sso_integration_for_authorized_employees_only),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400,
                color = mutedGreen,
                lineHeight = 26.sp
            )
        }
    }
}