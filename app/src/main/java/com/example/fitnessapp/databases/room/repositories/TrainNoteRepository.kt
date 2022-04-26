package com.example.fitnessapp.databases.room.repositories

import com.example.fitnessapp.databases.interfaces.TrainNoteDbInterface
import com.example.fitnessapp.databases.room.dao.TrainNoteDao
import com.example.fitnessapp.databases.room.entities.TrainNote
import io.reactivex.rxjava3.core.Maybe

class TrainNoteRepository(private val trainNoteDao: TrainNoteDao): TrainNoteDbInterface {
    override fun getTrainNotesByDate(date: String): Maybe<List<TrainNote>> {
        return trainNoteDao.getTrainNotesByDate(date)
    }

    override fun insertTrainNote(trainNote: TrainNote) {
        trainNoteDao.insertTrainNote(trainNote)
    }
}