package com.example.gastronomad2.ui.screens.createRestaurant

import android.net.Uri
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import com.example.gastronomad2.models.entities.RestaurantType
import com.example.gastronomad2.servises.db.implementations.MediaDbApi
import com.example.gastronomad2.servises.implementations.CurrentUserInfo
import com.example.gastronomad2.ui.GastonomadAppViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.Date

class CreateRestaurantViewModel private constructor(): GastonomadAppViewModel() {

    companion object{

        private var INSTANCE: CreateRestaurantViewModel? = null

        fun getInstance(): CreateRestaurantViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CreateRestaurantViewModel().also { INSTANCE = it }
            }
        }
    }

    var title = mutableStateOf("")
    var location = mutableStateOf("")
    var description = mutableStateOf("")
    var selectedType = mutableStateOf<RestaurantType?>(null)

    val restaurantTypes = RestaurantType.entries
    var expandedType = mutableStateOf(false)

    var lat = CurrentUserInfo.getInstance().get()?.latLng?.let { mutableDoubleStateOf(it.latitude) }
        private set
    var lng = CurrentUserInfo.getInstance().get()?.latLng?.let { mutableDoubleStateOf(it.longitude) }
        private set

    var selectedImagesUris = mutableStateOf<List<Uri>>(emptyList())

    fun validateFields(): Boolean {
        return when {
            title.value.isEmpty() -> {
                setError("Title cannot be empty")
                false
            }
            description.value.isEmpty() -> {
                setError("Description cannot be empty")
                false
            }
            location.value.isEmpty() -> {
                setError("Location cannot be empty")
                false
            }
            selectedType.value == null -> {
                setError("Restaurant type must be selected")
                false
            }
            else -> {
                clearError()
                true
            }
        }
    }



    val editingOpis = mutableStateOf(false)
    val isErrorOpis = mutableStateOf(false)
    val charLimitOpis = 150
    val opisFocusRequester = FocusRequester()
    val mDbApi = MediaDbApi()

    private fun setError(message: String) {
        isError.value = true
        errorMessage.value = message
    }

    val isError = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    private fun clearError() {
        isError.value = false
        errorMessage.value = ""
    }

    fun clearFields() {
        title.value = ""
        description.value = ""
        location.value = ""
        clearError()
        selectedImagesUris.value = emptyList<Uri>()
        lat = CurrentUserInfo.getInstance().get()?.latLng?.let { mutableDoubleStateOf(it.latitude) }
        lng = CurrentUserInfo.getInstance().get()?.latLng?.let { mutableDoubleStateOf(it.longitude) }
        selectedType.value = null
    }

    fun setCoordinates(latLng: LatLng){
        lat!!.value = latLng.latitude
        lng!!.value = latLng.longitude
    }

}