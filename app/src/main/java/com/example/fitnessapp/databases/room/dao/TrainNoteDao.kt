package com.example.fitnessapp.databases.room.dao

import androidx.room.*
import com.example.fitnessapp.databases.room.entities.TrainNote
import io.reactivex.rxjava3.core.Maybe

@Dao
interface TrainNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrainNote(trainNote:TrainNote)

    @Delete
    fun deleteTrainNote(trainNote: TrainNote)

    @Query("SELECT * FROM train_notes WHERE date == :date")
    fun getTrainNotesByDate(date:String): Maybe<List<TrainNote>>
}