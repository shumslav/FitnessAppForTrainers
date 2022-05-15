package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.ScheduleNoteCardBinding
import com.example.fitnessapp.viewModels.ScheduleViewModel

@SuppressLint("NotifyDataSetChanged")
class TrainNotesAdapter(val viewmodel: ScheduleViewModel, lifecycleOwner: LifecycleOwner):
    RecyclerView.Adapter<TrainNotesAdapter.TrainNotesHolder>() {

    init {
        viewmodel.trainNotes.observe(lifecycleOwner){
            notifyDataSetChanged()
        }
    }

    class TrainNotesHolder(val binding: ScheduleNoteCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainNotesHolder {
        return TrainNotesHolder(
            ScheduleNoteCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TrainNotesHolder, position: Int) {
        if (viewmodel.trainNotes.value!= null) {
            holder.binding.trainNote = viewmodel.trainNotes.value!![position]
            if(viewmodel.trainNotes.value!![position].isCompleted)
                holder.binding.status.text = "Выполнена"
            else
                holder.binding.status.text = "Не выполнена"
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.trainNotes.value?.size ?: 0
    }
}
