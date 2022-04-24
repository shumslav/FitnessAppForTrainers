package com.example.fitnessapp.models

class CalendarDay(val day: String, val weekDay:String, val month: String,val year: String) {

    fun equals(other: CalendarDay): Boolean {
        return (day==other.day && month==other.month && year==other.year)
    }
}