package com.example.fitnessapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.CalendarDay
import com.example.fitnessapp.models.TrainNote
import getDates
import getMonthFromNumber
import java.util.*

class ScheduleViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val datesData: MutableLiveData<MutableList<CalendarDay>> = MutableLiveData()
    val isAddNoticeVisible: MutableLiveData<Boolean> = MutableLiveData()
    val lastPickedDay: MutableLiveData<CalendarDay> = MutableLiveData()
    val trainNotes: MutableLiveData<MutableList<TrainNote>> = MutableLiveData()
    val calendarStart = Calendar.getInstance()
    val calendarEnd = Calendar.getInstance()
    var startDate = ""
    var endDate = ""

    init {
        isAddNoticeVisible.value = false
        calendarEnd.add(Calendar.DATE, 30)
        setDates()
    }

    fun getTrainNotes() {
    }

    fun setDates(){
        val startTime = calendarStart.time
        val endTime = calendarEnd.time
        startDate = startTime.toString().split(" ")[2] +
                ".${getMonthFromNumber(startTime.toString().split(" ")[1])}" +
                ".${startTime.toString().split(" ").last()}"
        endDate = endTime.toString().split(" ")[2] +
                ".${getMonthFromNumber(endTime.toString().split(" ")[1])}" +
                ".${endTime.toString().split(" ").last()}"
        Log.i("DATES", startDate)
        Log.i("DATES", endDate)
        datesData.value = getDates(startDate, endDate)
    }

}