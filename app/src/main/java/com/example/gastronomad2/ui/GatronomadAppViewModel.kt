package com.example.gastronomad2.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class GastonomadAppViewModel : ViewModel() {

    fun launchCatching(block: suspend CoroutineScope.() -> Unit) {

        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d(ERROR_TAG, throwable.message.orEmpty())
            },
            block = block
        )
    }

    companion object {
        const val ERROR_TAG = "KAD&GDE APP ERROR"
    }
}