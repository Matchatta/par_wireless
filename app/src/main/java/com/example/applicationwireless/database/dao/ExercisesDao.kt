package com.example.applicationwireless.database.dao

import androidx.room.*
import com.example.applicationwireless.database.entity.Exercises
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ExercisesDao {
    @Query("SELECT * FROM exercises WHERE email = :email")
    fun getExercisesByEmail(email: String): Flowable<List<Exercises>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercises(exercises: Exercises): Completable
    @Update
    fun updateExercises(exercises: Exercises): Completable
    @Delete
    fun deleteExercises(exercises: Exercises): Completable
}