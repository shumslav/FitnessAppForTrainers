package com.example.fitnessapp.databases.room.entities

import android.net.ParseException
import androidx.room.*
import com.example.fitnessapp.databases.models.Exercise
import com.example.fitnessapp.databases.room.typeconventers.ExercisesTypeConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "train_notes")
@TypeConverters(ExercisesTypeConverter::class)
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
    val exercises:List<Exercise>
){
    val duration:String
    get() {
        val df: DateFormat = SimpleDateFormat("hh:mm", Locale.US)
        return try {
            val date1: Date = df.parse(startTime)!!
            val date2: Date = df.parse(finishTime)!!
            val diff: Long = (date2.time - date1.time) / 1000
            "${diff/60} min"
        } catch (e:ParseException){
            "0 min"
        }
    }
}