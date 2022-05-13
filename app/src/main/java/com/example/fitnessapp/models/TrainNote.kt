package com.example.fitnessapp.models

import android.net.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class TrainNote(
    val id:Int,
    val date:String,
    val startTime:String,
    val finishTime:String,
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
            } catch (e: ParseException){
                "0 min"
            }
        }
}