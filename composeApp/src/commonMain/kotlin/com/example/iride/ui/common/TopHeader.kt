package com.example.iride.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.app_name
import com.example.iride.generated.resources.ic_notification
import com.example.iride.generated.resources.ic_user_profile
import com.example.iride.theme.emeraldGreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


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

@Preview
@Composable
fun TopHeaderPreview() {
    TopHeader({},{})
}