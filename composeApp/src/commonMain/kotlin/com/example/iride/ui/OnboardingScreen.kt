package com.example.iride.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.carbon_ledger
import com.example.iride.generated.resources.commute_together
import com.example.iride.generated.resources.corporate_credits
import com.example.iride.generated.resources.corporate_exclusive
import com.example.iride.generated.resources.ic_commute
import com.example.iride.generated.resources.ic_lock
import com.example.iride.generated.resources.ic_stars
import com.example.iride.generated.resources.ic_trees
import com.example.iride.generated.resources.ic_wallet
import com.example.iride.generated.resources.join_your_colleagues_in_reducing_co2
import com.example.iride.generated.resources.real_time_tracking_ofevery_gram_of_co2
import com.example.iride.generated.resources.secure_sso_integration_for_authorized_employees_only
import com.example.iride.generated.resources.the_smarter_way_to
import com.example.iride.generated.resources.turn_your_sustainability_efforts_into_tangible_corporate_rewards_and_perks
import com.example.iride.theme.darkGreen
import com.example.iride.theme.deepGreen
import com.example.iride.theme.lavenderBlue
import com.example.iride.theme.mintGreen
import com.example.iride.theme.mutedGreen
import com.example.iride.theme.primaryBackground
import com.example.iride.theme.primaryBlack
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    navigateToSignInScreen : () -> Unit
) {

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        delay(2000)
        navigateToSignInScreen()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBackground)
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryBackground)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 25.dp)
            ,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(RoundedCornerShape(50))
                    .background(mintGreen)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    painter = painterResource(Res.drawable.ic_stars),
                    contentDescription = "Stars",
                    modifier = Modifier
                        .size(17.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    stringResource(Res.string.corporate_exclusive).uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = darkGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.W600,
                        color = primaryBlack
                    )){
                        append(
                            stringResource(Res.string.the_smarter_way_to)
                        )
                    }

                    withStyle(style = SpanStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.W600,
                        color = darkGreen
                    )){
                        append(
                            stringResource(Res.string.commute_together)
                        )
                    }
                },
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(Res.string.join_your_colleagues_in_reducing_co2),
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                color = mutedGreen
            )

            Spacer(modifier = Modifier.height(40.dp))

            RideImpactCard(
                icon = painterResource(Res.drawable.ic_trees),
                title = stringResource(Res.string.carbon_ledger),
                description = stringResource(Res.string.real_time_tracking_ofevery_gram_of_co2)
            )

            Spacer(modifier = Modifier.height(16.dp))

            RideImpactCard(
                icon = painterResource(Res.drawable.ic_wallet),
                title = stringResource(Res.string.corporate_credits),
                description = stringResource(Res.string.turn_your_sustainability_efforts_into_tangible_corporate_rewards_and_perks)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
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
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W400,
                    color = mutedGreen
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Image(
                painter = painterResource(Res.drawable.ic_commute),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
                    .scale(1.2f),
                contentDescription = "Commute image",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}


@Composable
fun RideImpactCard(
    icon : Painter,
    title : String,
    description : String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = lavenderBlue
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Icon(
                painter = icon,
                contentDescription = "Impact Icon",
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(mintGreen)
                    .padding(10.dp)
                    .size(20.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.W700,
                color = deepGreen
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                fontWeight = FontWeight.W400,
                color = mutedGreen
            )
        }
    }
}