package com.example.applicationwireless.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity( tableName = "food", foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("email"),
        childColumns = arrayOf("email"),
        onDelete = ForeignKey.CASCADE)])
data class Food(
        @PrimaryKey(autoGenerate = true)
        val id : Int? =null,
        var name : String,
        var type : String,
        var date: String,
        var calories: Double,
        var note: String,
        var location: String,
        val quantity: Int,
        val email: String
)