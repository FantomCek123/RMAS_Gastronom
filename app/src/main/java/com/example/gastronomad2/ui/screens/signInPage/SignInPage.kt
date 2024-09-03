package com.example.gastronomad2.ui.screens.signInPage

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSignInPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    sipvm: SignInViewModel = SignInViewModel.getInstance(context)
) {
    val email = sipvm.email.collectAsState()
    val password = sipvm.password.collectAsState()
    val passwordVisible = sipvm.passwordVisible.collectAsState()

    Scaffold(
    ) { paddingValues ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.logo),
                contentDescription = "Auth image",
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(36.dp)
            )

            TextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = Purple40),
                        shape = RoundedCornerShape(50)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = email.value,
                onValueChange = { sipvm.updateEmail(it) },
                placeholder = { Text(stringResource(R.string.email)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email"
                    )
                }
            )

            TextField(
                singleLine = true,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 4.dp)
                    .border(
                        BorderStroke(width = 2.dp, color = Purple40),
                        shape = RoundedCornerShape(50)
                    ),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = password.value,
                onValueChange = { sipvm.updatePassword(it) },
                placeholder = { Text(stringResource(R.string.password)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password"
                    )
                },
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { sipvm.togglePasswordVisibility() }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible.value) R.drawable.show_password else R.drawable.hide_password
                            ),
                            contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                        )
                    }
                }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            Button(
                onClick = {
                    sipvm.onSignInClick(
                        navController = navController, // Prosledi navController
                        loadingScreen = Screen.Loading.name,
                        openAndPopUp = {
                            navController.popBackStack(Screen.SignIn.name, inclusive = true)
                            navController.navigate(Screen.Home.name)
                        }
                    )
                },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
            ) {
                Text(
                    text = stringResource(R.string.log_in),
                    fontSize = 16.sp,
                    modifier = modifier.padding(0.dp, 6.dp)
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )

            TextButton(onClick = {
                navController.popBackStack(Screen.SignIn.name, inclusive = false)
                navController.navigate(Screen.SignUp.name)
            }) {
                Text(text = stringResource(R.string.sign_up_description), fontSize = 16.sp)
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )

        }
    }
}

