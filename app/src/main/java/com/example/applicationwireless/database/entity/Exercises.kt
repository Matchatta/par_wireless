package com.example.applicationwireless.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "exercises", foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("email"),
        childColumns = arrayOf("email"),
        onDelete = ForeignKey.CASCADE)])
data class Exercises(
        @PrimaryKey(autoGenerate = true)
        val id : Int? =null,
        var name : String,
        var date: String,
        var calories: Double,
        var note: String,
        var location: String,
        var duration: Int,
        val email: String
)