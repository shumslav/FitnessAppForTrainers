package com.example.fitnessapp.ui.schedule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fitnessapp.models.CalendarDay
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel(application: Application):AndroidViewModel(application) {

    val datesData: MutableLiveData<MutableList<CalendarDay>> = MutableLiveData()
    val isAddNoticeVisible: MutableLiveData<Boolean> = MutableLiveData()
    val lastPickedDay: MutableLiveData<CalendarDay> = MutableLiveData()

    init {
        isAddNoticeVisible.value = false
        datesData.value = getDates("01.01.2022","01.12.2022")
        Log.i("MyError", "CreateDates")
    }

    private fun getDates(fromDate:String,toDate:String):MutableList<CalendarDay>{
        val dates = mutableListOf<CalendarDay>()
        val df1 = SimpleDateFormat("dd.MM.yyyy", Locale.US)

        var date1:Date?
        var date2:Date?

        try {
            date1 = df1.parse(fromDate)
            date2 = df1.parse(toDate)
            val cal1 = Calendar.getInstance()
            cal1.time = date1!!


            val cal2 = Calendar.getInstance()
            cal2.time = date2!!

            while(!cal1.after(cal2))
            {
                val time = cal1.time
                val weekday = time.toString().split(" ")[0]
                val day = time.toString().split(" ")[2]
                val month = time.toString().split(" ")[1]
                val year = time.toString().split(" ").last()
                dates.add(CalendarDay(day,weekday,month, year))
                cal1.add(Calendar.DATE, 1)
            }
        } catch (e: ParseException) {
            Log.i("ERROR", "ERROR")
            e.printStackTrace();
        }
        Log.i("Count days", "${dates.size}")
        return dates
    }

}