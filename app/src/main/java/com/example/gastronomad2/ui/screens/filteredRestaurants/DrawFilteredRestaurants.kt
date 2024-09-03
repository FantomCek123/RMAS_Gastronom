package com.example.gastronomad2.ui.screens.filteredRestaurants

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gastronomad2.R
import com.example.gastronomad2.servises.db.implementations.UserDbApi
import com.example.gastronomad2.ui.screens.components.DisplayRestaurants
import com.example.gastronomad2.ui.screens.filters.FilterPageViewModel
import com.example.gastronomad2.ui.screens.myRestaurants.MyRestaurantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DrawFilteredRestaurants(
    navController: NavController,
    frpvm: FilteredRestaurantsViewModel = FilteredRestaurantsViewModel.getInstance(),
    context: Context
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rezultati pretrage:") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DropdownMenu(
                expanded = frpvm.expanded.value,
                onDismissRequest = { frpvm.expanded.value = false },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Ograniči prikaz na 4 stavke i dodaj skrolovanje ako ima više
                Column(
                ) {
                    frpvm.listOfSortsMethods.forEach { sort ->
                        DropdownMenuItem(
                            onClick = {
                                frpvm.expanded.value = false
                                frpvm.sortRestaurants(sort)
                            },
                            text = { Text(text = sort) },
                            modifier = Modifier.height(48.dp) // Visina svakog itema
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // Poravnavanje dugmeta desno
            ) {
                IconButton(onClick = { frpvm.expanded.value = ! frpvm.expanded.value  }) {
                    Icon(
                        imageVector = Icons.Filled.Menu, // Koristite ikonu koja vam odgovara
                        contentDescription = "Favorite Icon",)
                }
            }


            Spacer(modifier = Modifier.height(5.dp))
            Divider(modifier = Modifier.padding(vertical = 5.dp))
            DisplayRestaurants(restaurants = frpvm.filteredRestaurants.value, navController = navController,"")
            Divider(modifier = Modifier.padding(vertical = 5.dp))
        }
    }
}