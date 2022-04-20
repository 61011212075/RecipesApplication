package com.example.android.recipeapplication.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite (
    @PrimaryKey( autoGenerate = true )
    val id: Int,
    val userId: String,
    val recipeId: String
)