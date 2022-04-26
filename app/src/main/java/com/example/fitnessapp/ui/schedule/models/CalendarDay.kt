package com.example.fitnessapp.models

class CalendarDay(val day: String, val weekDay:String, val month: String,val year: String) {

    fun equals(other: CalendarDay): Boolean {
        return (day==other.day && month==other.month && year==other.year)
    }

    val monthInt:Int
    get() {
        return when(month){
            "Jan" -> 1
            "Feb" -> 2
            "Mar" -> 3
            "Apr" -> 4
            "May" -> 5
            "Jun" -> 6
            "Jul" -> 7
            "Aug" -> 8
            "Sep" -> 9
            "Oct" -> 10
            "Nov" -> 11
            "Dec" -> 12
            else -> 0
        }
    }

    val dateString:String
    get() {
        return "$day.$"
    }
}