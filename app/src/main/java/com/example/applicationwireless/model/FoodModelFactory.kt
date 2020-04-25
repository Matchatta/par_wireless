package com.example.applicationwireless.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.applicationwireless.database.dao.FoodDao
import com.example.applicationwireless.database.dao.UserDao
import java.lang.IllegalArgumentException

class FoodModelFactory (private val dataSource : FoodDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FoodViewModel::class.java)){
            return FoodViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}