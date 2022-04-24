package com.example.fitnessapp.database.room.entities

import androidx.room.*
import com.example.fitnessapp.database.models.Exercise
import com.example.fitnessapp.database.room.typeconventers.ExercisesTypeConverter

@Entity(tableName = "train_notes")
data class TrainNote(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val date:String,
    val time:String,
    val duration:String,
    @ColumnInfo(name = "body_part")
    val bodyPart:String,
    @TypeConverters(ExercisesTypeConverter::class)
    val exercises:List<Exercise>
)