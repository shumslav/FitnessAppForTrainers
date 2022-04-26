package com.example.fitnessapp.databases.models

data class Exercise(
    val name:String,
    val bodyPart:String,
    val repetitions:Int,
    val liftedWeight:Double
)