package com.example.iride.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.app_name
import com.example.iride.generated.resources.carbon_footprint
import com.example.iride.generated.resources.carbon_ledger
import com.example.iride.generated.resources.estimated_carbon_saving
import com.example.iride.generated.resources.ic_leaf
import com.example.iride.generated.resources.ic_flag
import com.example.iride.generated.resources.ic_location
import com.example.iride.generated.resources.ic_notification
import com.example.iride.generated.resources.ic_publish_ride_map
import com.example.iride.generated.resources.ic_right_arrow
import com.example.iride.generated.resources.ic_seat
import com.example.iride.generated.resources.ic_time
import com.example.iride.generated.resources.ic_user_profile
import com.example.iride.generated.resources.offer_ride_sub_title
import com.example.iride.generated.resources.offer_ride_title
import com.example.iride.generated.resources.publish_ride
import com.example.iride.generated.resources.route_details
import com.example.iride.theme.darkBlue
import com.example.iride.theme.deepGreen
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.greyLight
import com.example.iride.theme.lightGreen
import com.example.iride.theme.mutedGreen
import com.example.iride.theme.paleGreen
import com.example.iride.theme.primaryBackground
import com.example.iride.theme.primaryBlack
import com.example.iride.theme.strokeLightGreen
import com.example.iride.ui.common.TopHeader
import com.example.iride.viewmodel.RideViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject

@Composable
fun FindRideScreen(
    rideViewModel: RideViewModel
) {

    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.fillMaxSize().background(primaryBackground)) {
            TopHeader({}, {})
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(primaryBackground)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = stringResource(Res.string.offer_ride_title), color = deepGreen,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.W400,
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        text = stringResource(Res.string.offer_ride_sub_title), color = mutedGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W400,
                    )

                    Card(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                            Text(
                                text = stringResource(Res.string.route_details).uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    ).border(
                                        width = 1.dp,
                                        color = greyLight,
                                        shape = RoundedCornerShape(8.dp)
                                    ).background(primaryBackground)
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clickable(true, onClick = {
                                        // open the location search page with autocomplete or map
                                    }),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .padding(vertical = 8.dp)
                                        .height(50.dp),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_location),
                                        contentDescription = null,
                                        tint = darkBlue,
                                        modifier = Modifier
                                            .width(10.dp)
                                            .height(12.dp)
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                            .width(1.dp)
                                            .background(greyLight)
                                            .height(16.dp)
                                    )

                                }

                                Text(
                                    "Downtown Tech District",
                                    color = darkBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W300,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                )
                            }


                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp)
                                    .padding(bottom = 8.dp)
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    ).border(
                                        width = 1.dp,
                                        color = greyLight,
                                        shape = RoundedCornerShape(8.dp)
                                    ).background(primaryBackground).fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable(true, onClick = {
                                    }),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .padding(vertical = 8.dp)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_flag),
                                        contentDescription = null,
                                        tint = emeraldGreen,
                                        modifier = Modifier
                                            .width(9.dp)
                                            .height(10.dp)
                                    )
                                }

                                Text(
                                    text = "Downtown Tech District",
                                    color = darkBlue,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W300,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .padding(vertical = 16.dp)
                                )
                            }

                            Row {
                                Column(modifier = Modifier.fillMaxWidth(0.5f)) {
                                    Text(
                                        "DEPARTURE",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                            .padding(top = 16.dp)
                                    )

                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                            .padding(top = 8.dp, bottom = 8.dp).clip(
                                                RoundedCornerShape(8.dp)
                                            ).border(
                                                width = 1.dp,
                                                color = greyLight,
                                                shape = RoundedCornerShape(8.dp)
                                            ).background(primaryBackground).fillMaxWidth()
                                            .wrapContentHeight()
                                            .clickable(true, onClick = {
                                                // open the location search page with autocomplete or map
                                            }),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(start = 8.dp)
                                                .padding(vertical = 8.dp)
                                                .fillMaxHeight(),
                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = vectorResource(Res.drawable.ic_time),
                                                contentDescription = null,
                                                tint = emeraldGreen,
                                                modifier = Modifier.size(10.dp)
                                            )

                                        }

                                        Text(
                                            "08:30 AM", color = darkBlue,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.W300,
                                            modifier = Modifier.padding(start = 8.dp)
                                                .padding(vertical = 16.dp)
                                        )
                                    }

                                }
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        "SEATS",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W500,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                            .padding(top = 16.dp)
                                    )

                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                            .padding(top = 8.dp, bottom = 8.dp).clip(
                                                RoundedCornerShape(8.dp)
                                            ).border(
                                                width = 1.dp,
                                                color = greyLight,
                                                shape = RoundedCornerShape(8.dp)
                                            ).background(primaryBackground).fillMaxWidth()
                                            .wrapContentHeight()
                                            .clickable(true, onClick = {
                                            }),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(start = 8.dp)
                                                .padding(vertical = 8.dp)
                                                .fillMaxHeight(),
                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = vectorResource(Res.drawable.ic_seat),
                                                contentDescription = null,
                                                tint = emeraldGreen,
                                                modifier = Modifier.size(10.dp)
                                            )
                                        }

                                        Text(
                                            "2 Seats", color = darkBlue,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.W300,
                                            modifier = Modifier.padding(start = 8.dp)
                                                .padding(vertical = 16.dp)
                                        )

                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                                        ) {
                                            Icon(
                                                imageVector = vectorResource(Res.drawable.ic_right_arrow),
                                                contentDescription = null,
                                                tint = greyLight,
                                                modifier = Modifier.rotate(90f),
                                            )
                                        }


                                    }

                                }
                            }

                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp)
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .clip(
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = strokeLightGreen,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .background(lightGreen.copy(.2f))
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable(true, onClick = {
                                        // open the location search page with autocomplete or map
                                    }),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .padding(vertical = 16.dp)
                                        .clip(CircleShape)
                                        .background(emeraldGreen)
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .size(16.dp),
                                        painter = painterResource(
                                            Res.drawable.ic_leaf
                                        ),
                                        contentDescription = null,
                                        tint = strokeLightGreen
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        stringResource(resource = Res.string.estimated_carbon_saving),
                                        color = Color((0xFF003527)),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W600,
                                        lineHeight = 16.sp
                                    )

                                    Text(
                                        text = stringResource(resource = Res.string.carbon_footprint),
                                        color = Color(0xFF0B513D),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        modifier = Modifier
                                            .padding(top = 4.dp)
                                    )
                                }

                            }

                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp,top = 8.dp, bottom = 16.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .fillMaxWidth()
                                    .background(deepGreen)
                                    .clickable{
                                        scope.launch {
                                            rideViewModel.publishRide(
                                                origin = "Meerut",
                                                destination = "Delhi",
                                                seats = 4,
                                                price = 150.0,
                                                startDateTime = 1778250273000,
                                                endDateTime = 1778261073000
                                            )
                                        } // TODO : Need to change the default values and take from the user
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(Res.string.publish_ride),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W600,
                                    modifier = Modifier.padding(vertical = 16.dp),
                                )
                            }
                        }
                    }


                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(resource = Res.drawable.ic_publish_ride_map),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds, // Stretches to fill the box
                            modifier = Modifier.matchParentSize() // Ensures image matches Box size
                        )
                        // Other UI elements (Text, Buttons, etc.) go here and will appear on top
                        Column(
                            modifier = Modifier.fillMaxWidth(0.5f)
                                .align(Alignment.TopEnd).padding(end = 8.dp, top = 8.dp)
                                .clip(RoundedCornerShape(8.dp)).background(paleGreen)
                        ) {

                            Row(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    end = 8.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                Canvas(modifier = Modifier.size(15.dp), onDraw = {
                                    drawCircle(color = emeraldGreen)
                                })
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(start = 8.dp, end = 8.dp),
                                    horizontalAlignment = Alignment.Start
                                ) {

                                    Text(
                                        "Optimal Eco-Route",
                                        modifier = Modifier.padding(start = 8.dp),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W500
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text(
                                            "12.5 km",
                                            modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500
                                        )
                                        Canvas(modifier = Modifier.size(5.dp), onDraw = {
                                            drawCircle(color = primaryBlack)
                                        })
                                        Text(
                                            "24 mins",
                                            modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500
                                        )
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun TopHeader(onProfileClick: () -> Unit, onNotificationClick: () -> Unit) {
    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier.background(Color.White).fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = Modifier.background(Color.White).weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_user_profile),
                    contentDescription = null,
                    tint = null,
                    modifier = Modifier.clip(CircleShape).clickable(true, onClick = {
                        onProfileClick()
                    })

                )

                Text(
                    modifier = Modifier.padding(all = 16.dp),
                    text = stringResource(Res.string.app_name), color = emeraldGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                )
            }
            Icon(
                painter = painterResource(Res.drawable.ic_notification),
                contentDescription = null,
                tint = null,
                modifier = Modifier.clickable(true, onClick = {
                    onNotificationClick()
                })
            )

        }
    }
}

@Preview(device = PIXEL_9)
@Composable
fun FindRidePreview() {
    val rideViewModel : RideViewModel = koinInject()
    FindRideScreen(
        rideViewModel = rideViewModel
    )
}