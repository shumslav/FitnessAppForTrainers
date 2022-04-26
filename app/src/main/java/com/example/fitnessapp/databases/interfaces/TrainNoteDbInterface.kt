package com.example.fitnessapp.databases.interfaces

import com.example.fitnessapp.databases.room.entities.TrainNote
import io.reactivex.rxjava3.core.Maybe

interface TrainNoteDbInterface {

    fun getTrainNotesByDate(date:String): Maybe<List<TrainNote>>

    fun insertTrainNote(trainNote: TrainNote)

}