package com.example.gastronomad2.ui.screens.createRestaurant

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.RestaurantType
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.servises.implementations.LocationService.Companion.locationUpdates
import com.example.gastronomad2.ui.screens.components.NewRestaurantLocationPreviewMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DrawCreateRestaurantPage(
    navController: NavHostController,
    context: Context
) {
    val crvm: CreateRestaurantViewModel = CreateRestaurantViewModel.getInstance()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showBannedDialog by remember { mutableStateOf(false) }

    val currentLocation = locationUpdates.collectAsState().value
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(currentLocation?.latitude ?: 10.0, currentLocation?.longitude ?: 10.0), 15f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kreiraj resotran") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.back),
                            contentDescription = "Nazad"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = crvm.title.value,
                    onValueChange = { crvm.title.value = it },
                    label = { Text("Glavni naslov") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 14.dp),

                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                )
                TextField(
                    value = crvm.location.value,
                    onValueChange = { crvm.location.value = it },
                    label = { Text("Adresa") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 14.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )

                //Spacer(modifier = Modifier.height(.dp))
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                )
                {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 6.dp)
                    )
                    {}

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                            .height(120.dp)

                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = crvm.selectedType.value?.name ?: "Izaberite Tip",
                            onValueChange = { },
                            label = { Text("Tip") },
                            trailingIcon = {
                                IconButton(onClick = {
                                    crvm.expandedType.value = !crvm.expandedType.value
                                }) {
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
                            expanded = crvm.expandedType.value,
                            onDismissRequest = { crvm.expandedType.value = false },
                            modifier = Modifier
                                .fillMaxWidth()  // Postavi širinu na maksimalnu
                        ) {
                            // Prikaz svih stavki
                            Column(
                                modifier = Modifier.fillMaxWidth() // Kolona koristi celu širinu
                            ) {
                                crvm.restaurantTypes.forEach { type ->
                                    DropdownMenuItem(
                                        onClick = {
                                            crvm.selectedType.value = type
                                            crvm.expandedType.value = false
                                        },
                                        text = { Text(text = type.name) },
                                        modifier = Modifier.height(48.dp) // Visina svakog itema
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                if (crvm.editingOpis.value) {
                    OutlinedTextField(
                        value = crvm.description.value,
                        onValueChange = { newOpis ->
                            if (newOpis.length <= crvm.charLimitOpis) {
                                crvm.description.value = newOpis
                                crvm.isErrorOpis.value = newOpis.isEmpty()
                            }
                        },
                        label = {
                            Text(
                                text = if (crvm.isErrorOpis.value) "Opis*" else "Opis",
                                color = Color.Black // Postavi boju teksta na crnu
                            )
                        },
                        supportingText = {
                            Text(
                                text = "Limit: ${crvm.description.value.length}/${crvm.charLimitOpis}",
                                textAlign = TextAlign.End,
                                color = Color.Black // Postavi boju podržavajućeg teksta na crnu
                            )
                        },
                        isError = crvm.isErrorOpis.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                            .focusRequester(crvm.opisFocusRequester),
                        trailingIcon = {
                            IconButton(onClick = { crvm.description.value = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear text")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                crvm.editingOpis.value = false
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp) // Postavi boju teksta unutar TextField-a na crnu
                    )
                    LaunchedEffect(Unit) {
                        crvm.opisFocusRequester.requestFocus()
                    }
                } else {
                    // Kada nije u režimu uređivanja, prikaži samo tekst i ikonu za uređivanje
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .border(
                                BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .clickable { crvm.editingOpis.value = true } // Klik na bilo koji deo Column menja režim u uređivanje
                    ) {
                        Text(
                            text = crvm.description.value,
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black), // Postavi boju teksta na crnu
                            modifier = Modifier.align(Alignment.Start),
                        )
                        IconButton(
                            onClick = { crvm.editingOpis.value = true },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = "Edit Opis",
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                AddRestaurantPictures()
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Oznaci adresu na mapi.",
                    modifier = Modifier.align(Alignment.Start)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    NewRestaurantLocationPreviewMap(navController, crvm)
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                            if (crvm.validateFields()) {
                                val uid = CurrentUserInfo.getInstance().get()!!.id
                                val eid = DbApi().addRestaurant(
                                    publisherId = uid,
                                    title = crvm.title.value,
                                    description = crvm.description.value,
                                    lat = crvm.lat!!.doubleValue,
                                    lng = crvm.lng!!.doubleValue,
                                    location = crvm.location.value,
                                    type = crvm.selectedType.value!!.name
                                )

                                crvm.selectedImagesUris.let {
                                    crvm.mDbApi.uploadRestaurantImages(it.value, uid, eid)
                                }

                                navController.popBackStack(
                                    route = Screen.Profile.name,
                                    inclusive = false
                                )
                                crvm.clearFields()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kreiraj događaj")
                }
                if (crvm.isError.value) {
                    Text(
                        text = crvm.errorMessage.value,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    )
}

