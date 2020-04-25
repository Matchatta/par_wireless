package com.example.applicationwireless.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.applicationwireless.database.dao.ExercisesDao
import com.example.applicationwireless.database.dao.FoodDao
import com.example.applicationwireless.database.dao.UserDao
import com.example.applicationwireless.database.entity.Exercises
import com.example.applicationwireless.database.entity.Food
import com.example.applicationwireless.database.entity.User

@Database(entities = [User::class, Food::class, Exercises::class], version = 1)
abstract class database: RoomDatabase(){

    abstract fun userDao(): UserDao
    abstract fun foodDao(): FoodDao
    abstract fun exercisesDao(): ExercisesDao
    companion object {
        @Volatile private var INSTANCE: database? = null

        fun getInstance(context: Context): database =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        database::class.java, "Sample.db")
                        .build()
    }
}