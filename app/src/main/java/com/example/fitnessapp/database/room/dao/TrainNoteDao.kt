package com.example.fitnessapp.database.room.dao

import androidx.room.*
import com.example.fitnessapp.database.room.entities.TrainNote

@Dao
interface TrainNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrainNote(trainNote:TrainNote)

    @Delete
    fun deleteTrainNote(trainNote: TrainNote)

    @Query("SELECT * FROM train_notes WHERE date == :date")
    fun getTrainNotesByDate(date:String):List<TrainNote>
}