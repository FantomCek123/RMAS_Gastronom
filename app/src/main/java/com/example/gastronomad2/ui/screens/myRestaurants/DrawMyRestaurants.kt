package com.example.gastronomad2.ui.screens.myRestaurants

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DrawMyRestaurants(
    navController: NavController,
    mrpvm: MyRestaurantsViewModel = MyRestaurantsViewModel.getInstance(),
    context: Context
) {
    UserDbApi().getRestaurantsForUser()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Moji restorani:") },
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
            Spacer(modifier = Modifier.height(5.dp))
            Divider(modifier = Modifier.padding(vertical = 5.dp))
            DisplayRestaurants(restaurants = mrpvm.myRestaurants.value, navController = navController,"")
            Divider(modifier = Modifier.padding(vertical = 5.dp))
        }
    }
}