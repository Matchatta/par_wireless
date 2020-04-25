package com.example.applicationwireless.model

import androidx.lifecycle.ViewModel
import com.example.applicationwireless.database.dao.ExercisesDao
import com.example.applicationwireless.database.dao.FoodDao
import com.example.applicationwireless.database.dao.UserDao
import com.example.applicationwireless.database.entity.Exercises
import com.example.applicationwireless.database.entity.Food
import com.example.applicationwireless.database.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

class ExercisesViewModel(private val dataSource: ExercisesDao): ViewModel()  {
    fun getExercises(email: String): Flowable<List<Exercises>>{
        return dataSource.getExercisesByEmail(email)
    }
    fun deleteExercises(exercises: Exercises): Completable{
        return dataSource.deleteExercises(exercises)
    }
    fun insertExercises(exercises: Exercises): Completable{
        return dataSource.insertExercises(exercises)
    }
}