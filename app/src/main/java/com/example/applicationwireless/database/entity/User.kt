package com.example.applicationwireless.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
        @PrimaryKey
        @ColumnInfo(name = "email") var email : String,
        var name: String,
        var password: String,
        var weight: Double,
        var height: Double,
        var image: String? = null
)