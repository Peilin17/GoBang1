package com.example.gobang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class Factory () : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}