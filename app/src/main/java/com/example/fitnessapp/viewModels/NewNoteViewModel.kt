package com.example.fitnessapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.AddedExercise
import java.util.*

class NewNoteViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val addedExercises: MutableLiveData<MutableList<AddedExercise>> = MutableLiveData()
    val isAddingNewExercise: MutableLiveData<Boolean> = MutableLiveData()

    val calendarStart = Calendar.getInstance()
    val calendarEnd = Calendar.getInstance()
    var startTime = ""
    var endTime = ""


    init {
        isAddingNewExercise.value = false
        addedExercises.value = mutableListOf()
    }

    fun getTime(calendar: Calendar): String {
        val hours = if (calendar.get(Calendar.HOUR_OF_DAY).toString().length == 1)
            "0${calendar.get(Calendar.HOUR_OF_DAY)}"
        else
            "${calendar.get(Calendar.HOUR_OF_DAY)}"
        val minutes = if (calendar.get(Calendar.MINUTE).toString().length == 1)
            "0${calendar.get(Calendar.MINUTE)}"
        else
            "${calendar.get(Calendar.MINUTE)}"
        return "${hours}:${minutes}"
    }

    fun addExercise(){
        isAddingNewExercise.value = true
    }

    fun cancelAddExercise(){
        isAddingNewExercise.value = false
    }
}