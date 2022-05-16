package com.example.fitnessapp.viewModels

import NODE_EXERCISES
import NODE_GROUP_MUSCLES
import NODE_USERS
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.AddedExercise
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.Exercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class NewNoteViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val addedExercises: MutableLiveData<MutableList<AddedExercise>> = MutableLiveData()
    val isAddingNewExercise: MutableLiveData<Boolean> = MutableLiveData()
    val groupsMuscle: MutableLiveData<MutableList<String>> = MutableLiveData()
    val user = CurrentClient(myApplication)
    val groupExercises: MutableLiveData<MutableMap<String, MutableList<Exercise>>> =
        MutableLiveData()
    val selectedGroup: MutableLiveData<String> = MutableLiveData()
    var selectedExercise: MutableLiveData<Exercise> = MutableLiveData()

    val calendarStart = Calendar.getInstance()
    val calendarEnd = Calendar.getInstance()
    var startTime = ""
    var endTime = ""


    init {
        getGroupsMuscleAndExercises()
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

    fun addExercise() {
        isAddingNewExercise.value = true
    }

    fun cancelAddExercise() {
        isAddingNewExercise.value = false
    }

    fun getGroupsMuscleAndExercises() {
        Firebase.database.reference
            .child(NODE_USERS)
            .child(user.login)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val groups = mutableListOf<String>()
                        val exercisesMap = mutableMapOf<String, MutableList<Exercise>>()
                        snapshot
                            .child(NODE_GROUP_MUSCLES)
                            .children.forEach {
                                val group = it.getValue(String::class.java)
                                if (!group.isNullOrEmpty()) {
                                    groups.add(group)
                                }
                            }
                        groups.forEach { group ->
                            val exercises = mutableListOf<Exercise>()
                            snapshot.child(NODE_EXERCISES).child(group).children.forEach { it1 ->
                                val exercise = it1.getValue(Exercise::class.java)
                                if (exercise != null)
                                    exercises.add(exercise)
                            }
                            exercisesMap[group] = exercises
                        }
                        groupsMuscle.value = groups
                        groupExercises.value = exercisesMap
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
    }
}