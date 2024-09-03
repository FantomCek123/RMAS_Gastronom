package com.example.gastronomad2.ui.screens.filters

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastronomad2.models.entities.RestaurantType
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.screens.components.NavigationBar
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawFilterPage(
    viewModel: FilterPageViewModel = FilterPageViewModel.getInstance(),
    navBar: NavigationBar,
    navController: NavController
) {


    val dateTime = LocalDateTime.now()
    val dateRangePickerState = remember {
        DateRangePickerState(
            initialSelectedStartDateMillis = LocalDateTime.of(2024,8,31,5,44).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            initialDisplayedMonthMillis = null,
            initialSelectedEndDateMillis = dateTime.toMillis(),
            initialDisplayMode = DisplayMode.Input,
            yearRange = (2010..2029),
            locale = CalendarLocale.ENGLISH
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Polje za unos naziva restorana

            Spacer(modifier = Modifier.height(32.dp))

            Divider(modifier = Modifier.padding(vertical = 5.dp))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.restaurantName.value,
                onValueChange = { viewModel.restaurantName.value = it },
                label = { Text("Naziv restorana") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Polje za izbor najmanje ocene
            Row( modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = viewModel.minimumRating.value?.toString() ?: "Izaberite ocenu",
                        onValueChange = { },
                        label = { Text("Najmanja ocena") },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.expandedRating.value = ! viewModel.expandedRating.value }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(75.dp)
                    )

                    // DropdownMenu
                    DropdownMenu(
                        expanded = viewModel.expandedRating.value,
                        onDismissRequest = { viewModel.expandedRating.value = false },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        // Ograniči prikaz na 4 stavke i dodaj skrolovanje ako ima više
                        Column(
                        ) {
                            viewModel.ratingOptions.forEach { rating ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.minimumRating.value = rating.toDouble()
                                        viewModel.expandedRating.value = false
                                    },
                                    text = { Text(text = rating.toString()) },
                                    modifier = Modifier.height(48.dp) // Visina svakog itema
                                )
                            }
                        }
                    }
                }


                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(120.dp)

                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value =    viewModel.selectedType.value?.name ?: "Izaberite Tip",
                        onValueChange = { },
                        label = { Text("Tip") },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.expandedType.value = !viewModel.expandedType.value }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(75.dp)
                    )

                    // DropdownMenu
                    DropdownMenu(
                        expanded = viewModel.expandedType.value,
                        onDismissRequest = { viewModel.expandedType.value = false },
                        modifier = Modifier
                            .fillMaxWidth()  // Postavi širinu na maksimalnu
                    ) {
                        // Prikaz svih stavki
                        Column(
                            modifier = Modifier.fillMaxWidth() // Kolona koristi celu širinu
                        ) {
                            viewModel.restaurantTypes.forEach { type ->
                                DropdownMenuItem(
                                    onClick = {
                                        viewModel.selectedType.value = type
                                        viewModel.expandedType.value = false
                                    },
                                    text = { Text(text = type.name) },
                                    modifier = Modifier.height(48.dp) // Visina svakog itema
                                )
                            }
                        }
                    }
                }
            }
            DateRangePickerScreen(dateRangePickerState)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.applayFilters()
                            navController.navigate(Screen.FilteredRestaurants.name)
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .size(230.dp), // Veliko okruglo dugme
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Filtriraj", color = Color.White, fontSize = 30.sp)
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            navBar.Draw(
                currentScreen = Screen.Filter // Example currentScreen, replace with actual state
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerScreen(dateRangePickerState : DateRangePickerState) {
    DateRangePicker(
        state = dateRangePickerState,
        modifier = Modifier.fillMaxWidth(),
        showModeToggle = false,
        headline = {
            Text(
                text = "Datum postavljanje restorana",
                fontSize = 16.sp
                //modifier = Modifier.padding(bottom = 8.dp)
            )
            /*DateRangePickerDefaults.DateRangePickerHeadline(
            state = dateRangePickerState,
            dateFormatter = DateRangePickerDefaults,
            modifier = Modifier.padding(vertical = 0.dp))*/
        }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()