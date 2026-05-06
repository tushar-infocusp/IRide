package com.example.iride.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.iride.data.CountryData
import com.example.iride.generated.resources.Res
import com.example.iride.generated.resources.ic_down_arrow
import com.example.iride.theme.emeraldGreen
import com.example.iride.theme.mutedGreen
import com.example.iride.theme.primaryBackground
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryCodeDropdown(
    modifier: Modifier,
    countries: List<CountryData>,
    onCountrySelected: (CountryData) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf(countries[0]) }
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        BasicTextField(
            value = "${selectedCountry.flag}\u00A0 ${selectedCountry.code}",
            onValueChange = {
                onCountrySelected(selectedCountry)
            },
            readOnly = true,
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.W500),
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
                .height(54.dp),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = "${selectedCountry.flag} ${selectedCountry.code}",
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    contentPadding = PaddingValues(start = 6.dp, end = 0.dp),
                    trailingIcon = {
                        Icon(
                            painter = painterResource(resource = Res.drawable.ic_down_arrow),
                            contentDescription = null,
                            modifier = Modifier
                                .size(width = 8.dp, height = 5.dp)
                                .rotate(if (expanded) 180f else 0f),
                            tint = Color.Unspecified
                        )
                    },
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = true,
                            isError = false,
                            interactionSource = interactionSource,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = emeraldGreen,
                                unfocusedBorderColor = Color(0xFFBFC9C3),
                                focusedContainerColor = primaryBackground,
                                unfocusedContainerColor = primaryBackground
                            ),
                            shape = RoundedCornerShape(12.dp),
                            focusedBorderThickness = 1.dp,
                            unfocusedBorderThickness = 1.dp,
                        )
                    }
                )
            }
        )


        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = { Text(" ${country.code}", fontSize = 14.sp) },
                    leadingIcon = { Text(text = country.flag, fontSize = 16.sp) },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    onClick = {
                        selectedCountry = country
                        expanded = false
                    }
                )
            }
        }
    }
}