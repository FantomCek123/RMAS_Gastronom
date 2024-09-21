package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gastronomad2.ui.screens.filters.FilterPageViewModel

@Composable
fun NumberSelector(
    modifier: Modifier = Modifier,
    min: Double = 0.00,
    max: Double = 100.0,
    step: Double = 0.01,

) {

    val fpvm = FilterPageViewModel.getInstance()

    var value = fpvm.radius

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Value: ${String.format("%.2f", value.value)}",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Slider(
            value = value.value.toFloat(),
            onValueChange = { newValue ->
                // Round the new value to the nearest step
                value.value = (newValue * 100).toInt() / 100.0
            },
            valueRange = min.toFloat()..max.toFloat(),
            steps = ((max - min) / step).toInt() - 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}