package com.example.fitnessapp.ui.schedule.Models

class CalendarDay(val day: Int,val month: Int,val year: Int) {

    val monthStr = when (month) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> "Error"
    }

}