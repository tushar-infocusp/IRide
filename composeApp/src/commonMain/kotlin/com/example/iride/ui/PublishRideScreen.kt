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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.data.showToast
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.app_name
import com.example.iride.generated.resources.carbon_footprint
import com.example.iride.generated.resources.estimated_carbon_saving
import com.example.iride.generated.resources.ic_flag
import com.example.iride.generated.resources.ic_leaf
import com.example.iride.generated.resources.ic_location
import com.example.iride.generated.resources.ic_notification
import com.example.iride.generated.resources.ic_publish_ride_map
import com.example.iride.generated.resources.ic_right_arrow
import com.example.iride.generated.resources.ic_seat
import com.example.iride.generated.resources.ic_time
import com.example.iride.generated.resources.ic_user_profile
import com.example.iride.generated.resources.offer_ride_departure_time_title
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
import com.example.iride.viewmodel.LocationViewModel
import com.example.iride.viewmodel.RideViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject

@Composable
fun FindRideScreen(
    rideViewModel: RideViewModel,
    locationViewModel: LocationViewModel = koinInject(),
    openMaps : (SearchType) -> Unit
) {

    val scope = rememberCoroutineScope()
    val pickupAddress by locationViewModel.pickupAddress.collectAsState()
    val dropoffAddress by locationViewModel.dropoffAddress.collectAsState()
    var publishingRide by remember { mutableStateOf(false) }
    val allRides by rideViewModel.allRides.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.fillMaxSize().background(primaryBackground)) {
            TopHeader({}, {})
            var showDatePicker by remember { mutableStateOf(false) }
            var departureDateTime by remember { mutableStateOf(LocalDateTime.now()) }
            var selectedSeatOption by remember { mutableStateOf(1) }

            val departureText by remember {
                derivedStateOf {
                    departureDateTime.let {
                        it.date.toString() + " " + it.time.hour.toString() + ":" + it.time.minute.toString()
                    }
                }
            }

            LaunchedEffect(Unit) {
                rideViewModel.fetchLocation()
            }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(primaryBackground)
            ) {
                if (showDatePicker) {
                    WheelDatePickerBottomSheet(stringResource(Res.string.offer_ride_departure_time_title)) {
                        if (it != null) {
                            departureDateTime = it
                        }
                        showDatePicker = false
                    }
                }
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
                            text = stringResource(Res.string.offer_ride_sub_title),
                            color = mutedGreen,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W400,
                        )

                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = "Total local rides: ${allRides.size}",
                            color = deepGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W600
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
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                        .padding(top = 16.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .padding(top = 8.dp, bottom = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = 1.dp,
                                            color = greyLight,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(primaryBackground)
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable { openMaps(SearchType.PICKUP) },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_location),
                                        contentDescription = null,
                                        tint = darkBlue,
                                        modifier = Modifier.padding(start = 12.dp).size(16.dp)
                                    )

                                    Text(
                                        text = pickupAddress?.displayName ?: "Select Pickup Location",
                                        color = darkBlue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W300,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 12.dp).weight(1f)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .padding(top = 4.dp, bottom = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .border(
                                            width = 1.dp,
                                            color = greyLight,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .background(primaryBackground)
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .clickable { openMaps(SearchType.DROPOFF) },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = vectorResource(Res.drawable.ic_flag),
                                        contentDescription = null,
                                        tint = emeraldGreen,
                                        modifier = Modifier.padding(start = 12.dp).size(16.dp)
                                    )

                                    Text(
                                        text = dropoffAddress?.displayName ?: "Select Destination",
                                        color = darkBlue,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.W300,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 12.dp).weight(1f)
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
                                                    showDatePicker = true
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
                                                departureText, color = darkBlue,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W300,
                                                maxLines = 1,
                                                modifier = Modifier.padding(start = 8.dp)
                                                    .padding(vertical = 16.dp)
                                            )
                                        }

                                    }
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        val options = listOf(1, 2, 3, 4)
                                        var showSeatSelection by remember { mutableStateOf(false) }

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
                                                    showSeatSelection = true
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
                                                if (selectedSeatOption <= 1) {
                                                    "$selectedSeatOption Seat"
                                                } else {
                                                    "$selectedSeatOption Seats"
                                                }, color = darkBlue,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.W300,
                                                modifier = Modifier.padding(start = 8.dp)
                                                    .padding(vertical = 16.dp)
                                            )

                                            DropdownMenu(
                                                expanded = showSeatSelection,
                                                onDismissRequest = {
                                                    showSeatSelection = false
                                                }
                                            ) {
                                                options.forEach { option ->
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                if (option <= 1) {
                                                                    "$option Seat"
                                                                } else {
                                                                    "$option Seats"
                                                                },
                                                                color = darkBlue,
                                                                fontSize = 12.sp,
                                                                fontWeight = FontWeight.W300,
                                                            )
                                                        },
                                                        onClick = {
                                                            selectedSeatOption = option
                                                            showSeatSelection = false
                                                        }
                                                    )
                                                }
                                            }

                                            Row(
                                                horizontalArrangement = Arrangement.End,
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(end = 16.dp)
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
                                        .padding(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 8.dp,
                                            bottom = 16.dp
                                        )
                                        .clip(RoundedCornerShape(12.dp))
                                        .fillMaxWidth()
                                        .background(deepGreen)
                                        .height(56.dp)
                                        .clickable(!publishingRide) {
                                            scope.launch {
                                                publishingRide = true
                                                val result = rideViewModel.publishRide(
                                                    origin = pickupAddress?.displayName ?: "",
                                                    destination = dropoffAddress?.displayName ?: "",
                                                    seats = selectedSeatOption,
                                                    price = 150.0,
                                                    startDateTime = departureDateTime.toInstant(
                                                        TimeZone.currentSystemDefault()
                                                    ).epochSeconds,
                                                    endDateTime = departureDateTime.toInstant(
                                                        TimeZone.currentSystemDefault()
                                                    ).epochSeconds
                                                )
                                                if(result){
                                                    showToast("Ride Published successfully")
                                                }
                                                else{
                                                    showToast("Failed to publish ride")
                                                }
                                                publishingRide = false
                                            }
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    if(!publishingRide){
                                        Text(
                                            text = stringResource(Res.string.publish_ride),
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.W600,
                                        )
                                    }
                                    else{
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                    }
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
                                                modifier = Modifier.padding(
                                                    start = 4.dp,
                                                    end = 4.dp
                                                ),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.W500
                                            )
                                            Canvas(modifier = Modifier.size(5.dp), onDraw = {
                                                drawCircle(color = primaryBlack)
                                            })
                                            Text(
                                                "24 mins",
                                                modifier = Modifier.padding(
                                                    end = 4.dp,
                                                    start = 4.dp
                                                ),
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


@Composable
fun WheelDatePickerBottomSheet(title: String, dateOfBirth: (LocalDateTime?) -> Unit) {

    var showDatePicker by remember { mutableStateOf(true) }
    var selectedDate by remember { mutableStateOf("") }


    WheelDateTimePickerView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 18.dp),
        showDatePicker = showDatePicker,
        title = title,
        doneLabel = "Done",
        titleStyle = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = emeraldGreen,
        ),
        doneLabelStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight(600),
            color = emeraldGreen,
        ),

        selectorProperties = WheelPickerDefaults.selectorProperties(
            borderColor = Color.LightGray,
        ),
        rowCount = 5,
        height = 180.dp,
        dragHandle = {

        },
        shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp),
        dateTimePickerView = DateTimePickerView.BOTTOM_SHEET_VIEW,
        onDoneClick = {

            dateOfBirth.invoke(it)
        },
        showMonthAsNumber = true,
        onDismiss = {
            dateOfBirth.invoke(null)
        }
    )
}

@Preview(device = PIXEL_9)
@Composable
fun PublishRidePreview() {
    val rideViewModel: RideViewModel = koinInject()
    PublishRideScreen(
        rideViewModel = rideViewModel
    ){

    }
}