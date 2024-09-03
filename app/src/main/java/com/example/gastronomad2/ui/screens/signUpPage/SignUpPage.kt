package com.example.gastronomad2.ui.screens.signUpPage

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.theme.Purple40


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawSignUpPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    supvm: SignUpPageViewModel = SignUpPageViewModel.getInstance(context)
) {
    Scaffold { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val email = supvm.email.collectAsState()
                val password = supvm.password.collectAsState()
                val confirmPassword = supvm.confirmPassword.collectAsState()
                val firstname = supvm.firstName.collectAsState()
                val lastname = supvm.lastName.collectAsState()
                val userName = supvm.userName.collectAsState()
                val number = supvm.number.collectAsState()

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp)
                )

                OutlinedTextField(
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
                    value = firstname.value,
                    onValueChange = { supvm.updateFirstName(it) },
                    placeholder = { Text(stringResource(R.string.name)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.name)
                        )
                    }
                )

                OutlinedTextField(
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
                    value = lastname.value,
                    onValueChange = { supvm.updateLastName(it) },
                    placeholder = { Text(stringResource(R.string.surname)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = stringResource(R.string.surname)
                        )
                    }
                )

                OutlinedTextField(
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
                    value = number.value,
                    onValueChange = { supvm.updateNumber(it) },
                    placeholder = { Text(stringResource(R.string.number)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = stringResource(R.string.number)
                        )
                    }
                )

                OutlinedTextField(
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
                    value = userName.value,
                    onValueChange = { supvm.updateUserName(it) },
                    placeholder = { Text("Userame") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription ="Userame"
                        )
                    }
                )

                OutlinedTextField(
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
                    onValueChange = { supvm.updateEmail(it) },
                    placeholder = { Text(stringResource(R.string.email)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = stringResource(R.string.email)
                        )
                    }
                )

                OutlinedTextField(
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
                    onValueChange = { supvm.updatePassword(it) },
                    placeholder = { Text(stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = R.string.password.toString()
                        )
                    },
                    visualTransformation = PasswordVisualTransformation()
                )

                OutlinedTextField(
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
                    value = confirmPassword.value,
                    onValueChange = { supvm.updateConfirmPassword(it) },
                    placeholder = { Text(stringResource(R.string.confirm_password)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = R.string.confirm_password.toString()
                        )
                    },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }

            ClickableText(
                text = AnnotatedString(stringResource(R.string.next)),
                onClick = {
                    navController.navigate(Screen.ProfilePicture.name)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
                style = androidx.compose.ui.text.TextStyle(
                    color = Purple40,
                    fontSize = 20.sp,
                )
            )
        }
    }
}