package com.example.applicationwireless

import android.content.Context
import com.example.applicationwireless.database.dao.ExercisesDao
import com.example.applicationwireless.database.dao.FoodDao
import com.example.applicationwireless.database.dao.UserDao
import com.example.applicationwireless.database.database
import com.example.applicationwireless.model.ExercisesModelFactory
import com.example.applicationwireless.model.FoodModelFactory
import com.example.applicationwireless.model.UserModelFactory

object Injection {
    private fun provideUserDataSource(context: Context): UserDao {
        val database = database.getInstance(context)
        return database.userDao()
    }

    fun provideUserModelFactory(context: Context): UserModelFactory {
        val dataSource = provideUserDataSource(context)
        return UserModelFactory(dataSource)
    }

    private fun provideFoodDataSource(context: Context): FoodDao {
        val database = database.getInstance(context)
        return database.foodDao()
    }

    fun provideFoodModelFactory(context: Context): FoodModelFactory {
        val dataSource = provideFoodDataSource(context)
        return FoodModelFactory(dataSource)
    }

    private fun provideExercisesDataSource(context: Context): ExercisesDao {
        val database = database.getInstance(context)
        return database.exercisesDao()
    }

    fun provideExercisesModelFactory(context: Context): ExercisesModelFactory {
        val dataSource = provideExercisesDataSource(context)
        return ExercisesModelFactory(dataSource)
    }
}