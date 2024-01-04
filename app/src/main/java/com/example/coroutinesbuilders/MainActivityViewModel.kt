package com.example.coroutinesbuilders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    init {

        viewModelScope.launch {
            // viewModelScope attached with viewModel With attached with android component like activity
            // when will component get destroy then coroutines under scope will automatically get cancelled
            while (true){
                delay(500)
                Log.d("viewModelScope","Hello from viewModelScope")
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.d("viewModelScope","ViewModel Destroyed")
    }
}