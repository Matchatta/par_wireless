package com.example.applicationwireless.database.dao

import androidx.room.*
import com.example.applicationwireless.database.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserByEmail(email: String): Flowable<User>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User): Completable
    @Update
    fun updateUser(user: User): Completable
}