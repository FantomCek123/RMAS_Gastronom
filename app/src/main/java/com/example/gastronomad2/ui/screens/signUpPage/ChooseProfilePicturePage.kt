package com.example.gastronomad2.ui.screens.signUpPage

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gastronomad2.R
import com.example.gastronomad2.models.entities.Screen
import com.example.gastronomad2.ui.screens.signInPage.SignInViewModel
import com.example.gastronomad2.ui.theme.Purple40

@Composable
fun DrawChooseProfilePicturePage(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    supvm: SignUpPageViewModel = SignUpPageViewModel.getInstance(context),
    sinvm: SignInViewModel = SignInViewModel.getInstance(context))
{
    //var uri by remember { mutableStateOf<Uri?>(supvm.profilePicture) }

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            supvm.profilePicture = it
        }
    )
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center // Center the content inside this Box
        ) {
            Column(
                modifier = Modifier

            )
            {

                AsyncImage(
                    model =  supvm.profilePicture,
                    contentDescription = null,
                    modifier = Modifier
                        .size(284.dp)
                        .border(width = 3.dp, color = Color.Gray)
                )

                Button(
                    onClick = {
                        singlePhotoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                {
                    Text(stringResource(id = R.string.changeProfilePic))
                }

            }

            ClickableText(
                text = AnnotatedString(stringResource(R.string.back)),
                onClick = {
                        navController.popBackStack(Screen.ProfilePicture.name, inclusive = true)
                        navController.navigate(Screen.SignUp.name)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24 .dp),
                style = androidx.compose.ui.text.TextStyle(
                    color = Purple40,
                    fontSize = 20.sp,
                )
            )

            ClickableText(
                text = AnnotatedString(stringResource(R.string.sign_up )),
                onClick = {
                    supvm.onSignUpClick(
                        navController = navController, // Prosledi navController
                        loadingScreen = Screen.Loading.name,
                        openAndPopUp = {
                            navController.popBackStack(Screen.ProfilePicture.name, inclusive = true)
                            navController.navigate(Screen.SignIn.name)
                        }
                    )
                },

                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24 .dp),
                style = androidx.compose.ui.text.TextStyle(
                    color = Purple40,
                    fontSize = 20.sp,
                )
            )

        }
    }
}
