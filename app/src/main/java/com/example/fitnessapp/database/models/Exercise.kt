package com.example.fitnessapp.database.models

data class Exercise(
    val name:String,
    val bodyPart:String,
    val repetitions:Int,
    val liftedWeight:Double
)