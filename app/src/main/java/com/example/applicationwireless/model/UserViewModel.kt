package com.example.applicationwireless.model

import androidx.lifecycle.ViewModel
import com.example.applicationwireless.database.dao.UserDao
import com.example.applicationwireless.database.entity.User
import io.reactivex.Completable
import io.reactivex.Flowable

class UserViewModel(private val dataSource: UserDao): ViewModel()  {
    fun getUser(email: String): Flowable<User>{
        return dataSource.getUserByEmail(email)
    }
    fun updateUser(user: User): Completable{
        return dataSource.updateUser(user)
    }
    fun insertUser(user: User): Completable{
        return dataSource.insertUser(user)
    }
}