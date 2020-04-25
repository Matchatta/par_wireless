package com.example.applicationwireless.model

import androidx.lifecycle.ViewModel
import com.example.applicationwireless.database.dao.FoodDao
import com.example.applicationwireless.database.dao.UserDao
import com.example.applicationwireless.database.entity.Food
import com.example.applicationwireless.database.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

class FoodViewModel(private val dataSource: FoodDao): ViewModel()  {
    fun getFood(email: String): Flowable<List<Food>>{
        return dataSource.getFoodByEmail(email)
    }
    fun deleteFood(food: Food): Completable{
        return dataSource.deleteFood(food)
    }
    fun insertFood(food: Food): Completable{
        return dataSource.insertFood(food)
    }
}