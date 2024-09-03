package com.example.gastronomad2.ui.screens.test

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.servises.implementations.LocationService
import com.example.gastronomad2.servises.test.RunningService

/*
@Composable
fun DrawButtons(
    context: Context
) {
    Column(
        modifier = Modifier
            .padding(16.dp) // Dodajte margine oko kolone
    ) {
        Button(
            onClick = {
                Intent(context, RunningService::class.java).apply {
                    action = RunningService.START
                    startService(this)
                }
            }
        ) {
            Text(text = "Start")
        }

        Button(
            modifier = Modifier.padding(top = 8.dp), // Dodajte razmak između dugmadi
            onClick = {
                Intent(context, RunningService::class.java).also {
                    it.action = RunningService.Actions.START.toString()
                    startService(this)
                }
            }
        ) {
            Text(text = "Stop")
        }
    }
}*/