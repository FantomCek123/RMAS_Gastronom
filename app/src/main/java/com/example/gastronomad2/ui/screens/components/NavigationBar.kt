package com.example.gastronomad2.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.implementations.CurrentUserInfo

class NavigationBar(
    private val navigateToHomePage: () -> Unit,
   // private val navigateToSearchPage: () -> Unit,
    //private val navigateToEventRemindersPage: () -> Unit,
    private val navigateToFilterPage: () -> Unit,
    private val navigateToProfilePage: () -> Unit
) {
    @Composable
    fun Draw(currentScreen: Screen?) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 45.dp)
                .drawBehind {
                    val borderSize = 2.dp.toPx()
                    drawLine(
                        color = Color.Black, // Tamna boja granice
                        start = Offset(0f, borderSize / 2),
                        end = Offset(size.width, borderSize / 2),
                        strokeWidth = borderSize
                    )
                }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentScreen == Screen.Home) {

                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .weight(2f)
                            .background(Color.Transparent) // Ensuring transparent background
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }

                } else {
                    IconButton(
                        onClick = navigateToHomePage,
                        modifier = Modifier
                            .weight(2f)
                            .background(Color.Transparent) // Ensuring transparent background
                    ) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                }
                if (currentScreen == Screen.Filter) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .weight(2f)
                            .background(Color.Transparent) // Ensuring transparent background
                    ) {
                        Icon(Icons.Default.List, contentDescription = "Filter")
                    }
                } else {
                    IconButton(
                        onClick = navigateToFilterPage,
                        modifier = Modifier
                            .weight(2f)
                            .background(Color.Transparent) // Ensuring transparent background
                    ) {
                        Icon(Icons.Default.List, contentDescription = "Filter")
                    }
                }

                if (currentScreen == Screen.Profile) {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .weight(2f)
                            .background(Color.Transparent) // Ensuring transparent background
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                } else {
                    IconButton(
                        onClick = navigateToProfilePage,
                        modifier = Modifier
                            .weight(2f)
                            .background(Color.Transparent) // Ensuring transparent background
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }

                /*
                IconButton(
                    onClick = navigateToProfilePage,
                    modifier = Modifier
                        .weight(2f)
                        .background(Color.Transparent) // Ensuring transparent background
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Profile")
                }*/
            }
        }
    }
}