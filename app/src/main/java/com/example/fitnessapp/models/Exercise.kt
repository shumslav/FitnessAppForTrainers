package com.example.fitnessapp.models

import com.example.fitnessapp.enums.Calculation

data class Exercise(
    var bodyPart:String = "",
    var name:String = "",
    var calculation: Calculation = Calculation.REPETITIONS,
    var isHaveWeight: Boolean = true
)