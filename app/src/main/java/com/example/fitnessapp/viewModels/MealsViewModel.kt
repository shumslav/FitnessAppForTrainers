package com.example.fitnessapp.viewModels

import NODE_MEALS
import NODE_USERS
import android.app.Application
import android.app.TimePickerDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.Meal
import com.example.fitnessapp.models.SomeMeal
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class MealsViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val meals: MutableLiveData<MutableMap<String, Meal>> = MutableLiveData()
    val user = CurrentClient(myApplication)
    var selectedMeals: MutableLiveData<MutableList<SomeMeal>> = MutableLiveData()
    val calendar = Calendar.getInstance()

    val daysOfWeek =
        listOf("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье")


    init {
        getMeals()
    }

    private fun getMeals() {
        Firebase.database.reference
            .child(NODE_USERS)
            .child(user.login)
            .child(NODE_MEALS).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val currentMeals = mutableMapOf<String, Meal>()
                        daysOfWeek.forEach { day ->
                            if (snapshot.hasChild(day))
                                currentMeals[day] = snapshot.child(day).getValue(Meal::class.java)!!
                            else
                                currentMeals[day] = Meal()
                        }
                        meals.value = currentMeals
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
    }

    fun sortMeals(meals: MutableList<SomeMeal>): MutableList<SomeMeal> {
        val sortedMeals = mutableListOf<SomeMeal>()
        val copyMeals = mutableListOf<SomeMeal>()
        if (meals.isEmpty())
            return sortedMeals
        copyMeals.addAll(meals)
        while (copyMeals.isNotEmpty()) {
            var minMeal: SomeMeal? = null
            copyMeals.forEach {
                if (minMeal == null)
                    minMeal = it
                else
                    minMeal = checkMeals(it,minMeal!!)
            }
            sortedMeals.add(minMeal!!)
            copyMeals.remove(minMeal)
        }
        return sortedMeals
    }

    private fun checkMeals(firstMeal: SomeMeal, secondMeal: SomeMeal): SomeMeal {
        val firstTime = firstMeal.time.split(":")
        val secondTime = secondMeal.time.split(":")
        if (firstTime[0][0]=='0')
            firstTime[0].removeRange(0,0)
        if (secondTime[0][0]=='0')
            secondTime[0].removeRange(0,0)

        if (firstTime[1][0]=='0')
            firstTime[1].removeRange(0,0)
        if (secondTime[1][0]=='0')
            secondTime[1].removeRange(0,0)
        val hourFirst = firstTime[0].toInt()
        val minutesFirst = firstTime[1].toInt()
        val hourSecond = secondTime[0].toInt()
        val minutesSecond = secondTime[1].toInt()
        return if (hourFirst >= hourSecond)
            if (hourFirst == hourSecond)
                if (minutesFirst >= minutesSecond)
                    secondMeal
                else
                    firstMeal
            else
                secondMeal
        else
            firstMeal
    }
}