package com.example.fitnessapp.database.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrainNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val date:String,
    val time:String,
    val duration:String,
    @ColumnInfo(name = "body_part")
    val bodyPart:String,
    val exercises:String
)