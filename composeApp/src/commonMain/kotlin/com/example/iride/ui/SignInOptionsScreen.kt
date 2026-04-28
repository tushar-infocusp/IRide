package com.example.iride.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources._35_percent_reduction
import com.example.iride.generated.resources.access_with_your_corporate_account
import com.example.iride.generated.resources.app_name
import com.example.iride.generated.resources.by_choosing_i_ride
import com.example.iride.generated.resources.ic_car
import com.example.iride.generated.resources.ic_i_ride
import com.example.iride.generated.resources.ic_message
import com.example.iride.generated.resources.ic_phone
import com.example.iride.generated.resources.ic_right_arrow
import com.example.iride.generated.resources.ic_tree
import com.example.iride.generated.resources.in_corporate_carbon_footprint
import com.example.iride.generated.resources.secure_otp_verification
import com.example.iride.generated.resources.sign_in_with_email
import com.example.iride.generated.resources.sign_in_with_phone
import com.example.iride.generated.resources.sustainability_commitment
import com.example.iride.generated.resources.your_journey_towards_zero_emission_mobility_begins_here
import com.example.iride.theme.darkGreen
import com.example.iride.theme.deepGreen
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.greyBlue
import com.example.iride.theme.mintGreen
import com.example.iride.theme.mutedGreen
import com.example.iride.theme.primaryBackground
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignInOptionsScreen() {

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            SignInOptionsTopBar()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
                .background(brush = Brush.radialGradient(
                    colors = listOf(
                        mintGreen.copy(alpha = 0.15f),
                        primaryBackground
                    ),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = 800f
                ))
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(56.dp))

            Icon(
                painter = painterResource(Res.drawable.ic_car),
                contentDescription = "Car Icon",
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(mintGreen)
                    .padding(horizontal = 30.dp, vertical = 26.dp)
                    .width(36.dp)
                    .height(44.dp),
                tint = darkGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.app_name),
                fontSize = 40.sp,
                fontWeight = FontWeight.W700,
                color = deepGreen
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.your_journey_towards_zero_emission_mobility_begins_here),
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                color = mutedGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            SignInCard(
                icon = painterResource(Res.drawable.ic_phone),
                title = stringResource(Res.string.sign_in_with_phone),
                description = stringResource(Res.string.secure_otp_verification)
            )

            Spacer(modifier = Modifier.height(14.dp))

            SignInCard(
                icon = painterResource(Res.drawable.ic_message),
                title = stringResource(Res.string.sign_in_with_email),
                description = stringResource(Res.string.access_with_your_corporate_account)
            )

            Spacer(modifier = Modifier.height(30.dp))

            SustainabilityCard()
        }
    }
}

@Composable
fun SignInOptionsTopBar(){
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
            painter = painterResource(Res.drawable.ic_i_ride),
            contentDescription = "I Ride Icon",
            modifier = Modifier
                .size(18.dp),
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
fun SignInCard(
    icon : Painter,
    title : String,
    description : String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp, Color(0xFFD1FAE5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(8f)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = icon,
                    contentDescription = "Sign In Icon",
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFD1FAE5))
                        .padding(horizontal = 17.dp, vertical = 13.dp)
                        .width(15.dp)
                        .height(22.dp)
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column(
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W600,
                        color = emeraldGreen,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.W500,
                        color = greyBlue,
                        lineHeight = 14.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(2f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(Res.drawable.ic_right_arrow),
                    contentDescription = "Arrow Icon",
                    modifier = Modifier
                        .width(8.dp)
                        .height(12.dp),
                    tint = mintGreen
                )
            }
        }
    }
}

@Composable
fun SustainabilityCard(){
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFD1FAE5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0XFFA7F3D0).copy(alpha = 0.15f),
                        Color(0xFFA7F3D0).copy(.5f)
                    ),
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    radius = 400f
                ))
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_tree),
                    contentDescription = "Tree Icon",
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFD1FAE5))
                        .padding(11.dp)
                        .width(11.dp)
                        .height(12.dp),
                    tint = Color(0xFF059669)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(Res.string.sustainability_commitment).uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W600,
                    color = Color(0xFF065F46)
                )
            }

            Spacer(modifier = Modifier.height(11.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = Color(0xFF047857)
                    )){
                        append(stringResource(Res.string.by_choosing_i_ride))
                    }

                    withStyle(SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = Color(0xFF065F46)
                    )){
                        append(" " +stringResource(Res.string._35_percent_reduction) + " ")
                    }

                    withStyle(SpanStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = Color(0xFF047857)
                    )){
                        append(stringResource(Res.string.in_corporate_carbon_footprint))
                    }
                },
            )

        }
    }
}