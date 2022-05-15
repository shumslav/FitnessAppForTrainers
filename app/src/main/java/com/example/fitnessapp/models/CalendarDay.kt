package com.example.fitnessapp.models

class CalendarDay(val day: String, val weekDay:String, val month: String,val year: String) {

    fun equals(other: CalendarDay): Boolean {
        return (day==other.day && month==other.month && year==other.year)
    }

    val monthIntStr:String
    get() {
        return when(month) {
            "Jan" -> "01"
            "Feb" -> "02"
            "Mar" -> "03"
            "Apr" -> "04"
            "May" -> "05"
            "Jun" -> "06"
            "Jul" -> "07"
            "Aug" -> "08"
            "Sep" -> "09"
            "Oct" -> "10"
            "Nov" -> "11"
            "Dec" -> "12"
            else -> ""
        }
    }

    val dateString:String
    get() {
        return "$day:$monthIntStr:$year"
    }
}