package com.example.fitnessapp.database.interfaces

import com.example.fitnessapp.database.room.entities.TrainNote

interface TrainNoteDbInterface {
    fun getTranNotesByDate(date:String):List<TrainNote>
}