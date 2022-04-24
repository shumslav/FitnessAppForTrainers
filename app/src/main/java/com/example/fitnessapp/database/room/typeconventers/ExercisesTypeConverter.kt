package com.example.fitnessapp.database.room.typeconventers

import androidx.room.TypeConverter
import com.example.fitnessapp.database.models.Exercise

class ExercisesTypeConverter {

    @TypeConverter
    fun toExercises(exercisesString: String): List<Exercise> {
        val data = exercisesString.split(',')
        val exercises = mutableListOf<Exercise>()
        data.forEach {
            val dataList = it.split(";")
            exercises.add(
                Exercise(
                    dataList[0],
                    dataList[1],
                    dataList[2].toInt(),
                    dataList[3].toDouble()
                )
            )
        }
        return exercises
    }

    @TypeConverter
    fun fromExercises(exercises: List<Exercise>): String {
        var exercisesString = ""
        for (i in 0..exercises.size) {
            val exercise = exercises[i]
            exercisesString += "${exercise.name};${exercise.bodyPart};${exercise.repetitions};${exercise.liftedWeight}"
            if (i != exercises.size-1 && exercises.size!=1)
                exercisesString+=","
        }
        return exercisesString
    }

}