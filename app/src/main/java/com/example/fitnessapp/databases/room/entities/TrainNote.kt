package com.example.fitnessapp.databases.room.entities

import androidx.room.*
import com.example.fitnessapp.databases.models.Exercise
import com.example.fitnessapp.databases.room.typeconventers.ExercisesTypeConverter

@Entity(tableName = "train_notes")
data class TrainNote(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val date:String,
    @ColumnInfo(name = "start_time")
    val startTime:String,
    @ColumnInfo(name = "finish_time")
    val finishTime:String,
    @ColumnInfo(name = "body_part")
    val bodyPart:String,
    @TypeConverters(ExercisesTypeConverter::class)
    val exercises:List<Exercise>
)