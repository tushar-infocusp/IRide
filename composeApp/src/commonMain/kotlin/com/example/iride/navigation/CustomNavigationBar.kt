package com.example.iride.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.paleGreen

@Composable
fun CustomBottomBar() {

    val tabNavigator = LocalTabNavigator.current

    val tabs = listOf(HomeTab, FindRideTab, OfferRideTab, ImpactTab, ProfileTab)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                clip = false
            )
            .background(Color.White)
            .navigationBarsPadding()
            .padding(vertical = 14.dp, horizontal = 17.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        tabs.forEach { tab ->

            val isSelected = tabNavigator.current == tab

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { tabNavigator.current = tab }
                    .background(if(isSelected) paleGreen else Color.White)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .weight(1f)
            ) {

                Icon(
                    painter = tab.options.icon!!,
                    contentDescription = tab.options.title,
                    tint = if (isSelected) emeraldGreen else Color(0xFF94A3B8),
                    modifier = Modifier
                        .width(14.dp)
                        .height(15.dp)
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = tab.options.title,
                    color = if (isSelected) emeraldGreen else Color(0xFF94A3B8),
                    fontWeight = FontWeight.W600,
                    fontSize = 10.sp
                )
            }
        }
    }
}