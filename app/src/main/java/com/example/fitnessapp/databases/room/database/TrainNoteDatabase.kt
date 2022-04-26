package com.example.fitnessapp.databases.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitnessapp.databases.room.dao.TrainNoteDao
import com.example.fitnessapp.databases.room.entities.TrainNote

@Database(entities = [TrainNote::class], version = 1)
abstract class TrainNoteDatabase : RoomDatabase() {
    abstract fun getTrainNoteDao(): TrainNoteDao

    companion object {
        var INSTANCE: TrainNoteDatabase? = null

        fun getTrainNoteDatabase(context: Context): TrainNoteDatabase {
            return if (INSTANCE == null) {
                synchronized(TrainNoteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        TrainNoteDatabase::class.java,
                        "myDB"
                    ).build()
                }
                INSTANCE!!
            } else
                INSTANCE!!
        }
    }
}