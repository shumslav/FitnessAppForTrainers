package com.example.fitnessapp.ui.schedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.ScheduleDayCardBinding
import com.example.fitnessapp.databinding.ScheduleNoteCardBinding
import com.example.fitnessapp.ui.schedule.ScheduleViewModel

class TrainNotesAdapter(val viewmodel: ScheduleViewModel, lifecycleOwner: LifecycleOwner):


    RecyclerView.Adapter<TrainNotesAdapter.TrainNotesHolder>() {

    class TrainNotesHolder(val binding: ScheduleNoteCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainNotesHolder {
        return TrainNotesAdapter.TrainNotesHolder(
            ScheduleNoteCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrainNotesHolder, position: Int) {
        if (viewmodel.trainNotes.value!= null) {
            holder.binding.trainNote = viewmodel.trainNotes.value!!.get(position)
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.trainNotes.value?.size ?: 0
    }
}
