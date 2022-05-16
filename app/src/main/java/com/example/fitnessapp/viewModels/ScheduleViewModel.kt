package com.example.fitnessapp.viewModels

import NODE_TRAIN_NOTES
import NODE_USERS
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.CalendarDay
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.TrainNote
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import getDates
import getMonthFromNumber
import java.util.*

class ScheduleViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val datesData: MutableLiveData<MutableList<CalendarDay>> = MutableLiveData()
    val isAddNoticeVisible: MutableLiveData<Boolean> = MutableLiveData()
    var lastPickedDay: MutableLiveData<CalendarDay?> = MutableLiveData()
    val trainNotes: MutableLiveData<MutableList<TrainNote>> = MutableLiveData()
    val calendarStart = Calendar.getInstance()
    val calendarEnd = Calendar.getInstance()
    var startDate = ""
    var endDate = ""
    val user = CurrentClient(myApplication)

    init {
        isAddNoticeVisible.value = false
        calendarEnd.add(Calendar.DATE, 30)
        setDates()
    }

    fun getTrainNotes() {
        if (lastPickedDay.value==null)
            return
        Firebase.database.reference
            .child(NODE_USERS)
            .child(user.login)
            .child(NODE_TRAIN_NOTES)
            .child(lastPickedDay.value!!.dateString).addListenerForSingleValueEvent(
                object : ValueEventListener{
                    val result = mutableListOf<TrainNote>()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            result.add(it.getValue(TrainNote::class.java)!!)
                        }
                        trainNotes.value = result
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
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
        datesData.value!!.forEach {
            if (lastPickedDay.value?.dateString==it.dateString)
                return
        }
        lastPickedDay.value = null
        isAddNoticeVisible.value = false
        trainNotes.value = mutableListOf()
    }

}