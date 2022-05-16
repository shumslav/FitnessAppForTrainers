package com.example.fitnessapp.models

import android.net.ParseException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class TrainNote(
    var id: Int = 0,
    var date: String = "",
    var startTime: String = "",
    var finishTime: String = "",
    var bodyPart: String = "",
    var exercises: MutableList<AddedExercise> = mutableListOf(),
    var notes: String = "",
    var isCompleted: Boolean = false
) {
    val duration: String
        get() {
            val df: DateFormat = SimpleDateFormat("hh:mm", Locale.US)
            return try {
                val date1: Date = df.parse(startTime)!!
                val date2: Date = df.parse(finishTime)!!
                val diff: Long = (date2.time - date1.time) / 1000
                "${diff / 60} min"
            } catch (e: ParseException) {
                "0 min"
            }
        }
    val dateWithDot: String
        get() {
            return date.replace(":", ".")
        }

    val allTime: String
        get() {
            return "${startTime}-${finishTime}"
        }
}