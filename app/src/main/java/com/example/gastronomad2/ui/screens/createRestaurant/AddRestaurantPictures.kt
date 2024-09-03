package com.example.gastronomad2.ui.screens.createRestaurant

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme

@Composable
fun AddRestaurantPictures(
    // eid: String,
){
    val crvm = CreateRestaurantViewModel.getInstance()
    var selectedImageUris by crvm.selectedImagesUris
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> selectedImageUris = uris }
    )

    Button(onClick = {
        multiplePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }) {
        Text(text = "Pick multiple photo")
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .border(
                BorderStroke(3.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            )

    ) {
        items(selectedImageUris) { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                // contentScale = ContentScale.Crop
            )
        }
    }

}