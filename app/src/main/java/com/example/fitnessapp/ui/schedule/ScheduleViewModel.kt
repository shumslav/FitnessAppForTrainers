package com.example.fitnessapp.ui.schedule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.databases.room.database.TrainNoteDatabase
import com.example.fitnessapp.databases.room.entities.TrainNote
import com.example.fitnessapp.databases.room.repositories.TrainNoteRepository
import com.example.fitnessapp.models.CalendarDay
import getDates
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class ScheduleViewModel(private val myApplication: Application):AndroidViewModel(myApplication) {

    private val compositeDisposable = CompositeDisposable()

    val datesData: MutableLiveData<MutableList<CalendarDay>> = MutableLiveData()
    val isAddNoticeVisible: MutableLiveData<Boolean> = MutableLiveData()
    val lastPickedDay: MutableLiveData<CalendarDay> = MutableLiveData()
    val trainNotes: MutableLiveData<MutableList<TrainNote>> = MutableLiveData()

    private var trainNoteRepository: TrainNoteRepository =
        TrainNoteRepository(TrainNoteDatabase.getTrainNoteDatabase(myApplication).getTrainNoteDao())

    init {
        isAddNoticeVisible.value = false
        datesData.value = getDates("01.01.2022","01.12.2022")
        Log.i("MyError", "CreateDates")
    }

    fun getTrainNotes(){
        if (lastPickedDay.value == null) return
        trainNoteRepository.getTrainNotesByDate(lastPickedDay.value!!.dateString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (!it.isNullOrEmpty()) {
                    trainNotes.postValue(it as MutableList<TrainNote>)
                } else {
                    trainNotes.postValue(mutableListOf())
                }
            }, {
            }).let {
                compositeDisposable.add(it)
            }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }
}