package com.example.iride.ui.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.theme.darkBlue
import com.example.iride.theme.emeraldGreen

@Composable
fun OtpInputField(
    otpLength: Int = 6,
    onOtpTextChange: (String) -> Unit
) {
    var otpValue by remember { mutableStateOf("") }

    BasicTextField(
        value = otpValue,
        onValueChange = {
            // Only allow numbers and limit length
            if (it.length <= otpLength && it.all { char -> char.isDigit() }) {
                otpValue = it
                onOtpTextChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(otpLength) { index ->
                    // Logic for display: char, hyphen, or empty
                    val displayChar = when {
                        index < otpValue.length -> otpValue[index].toString()
                        else -> "-" // Show hyphen for empty slots
                    }

                    val isFocused = otpValue.length == index
                    val isFilled = index < otpValue.length

                    OtpBox(
                        char = displayChar,
                        isFocused = isFocused,
                        isFilled = isFilled
                    )
                }
            }
        }
    )
}

@Composable
private fun OtpBox(
    char: String,
    isFocused: Boolean,
    isFilled: Boolean
) {
    Box(
        modifier = Modifier
            .size(48.dp, 56.dp)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) emeraldGreen else Color(0XFFBFC9C3),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.W500,
                textAlign = TextAlign.Center,
                // Make the hyphen slightly muted, and the digit darkBlue
                color = if (isFilled) darkBlue else Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}