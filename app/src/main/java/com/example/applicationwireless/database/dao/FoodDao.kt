package com.example.applicationwireless.database.dao

import androidx.room.*
import com.example.applicationwireless.database.entity.Food
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface FoodDao {
    @Query("SELECT * FROM food WHERE email = :email")
    fun getFoodByEmail(email: String): Flowable<List<Food>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: Food): Completable
    @Update
    fun updateFood(food: Food): Completable
    @Delete
    fun deleteFood(food: Food): Completable
}