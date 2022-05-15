package com.example.fitnessapp.models

data class AddedExercise(
    var name:String = "",
    val repetitions:String = "",
    val rounds:String = "",
    val liftedWeight:String? = null
)