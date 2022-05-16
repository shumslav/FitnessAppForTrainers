package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.ScheduleNoteCardBinding
import com.example.fitnessapp.ui.schedule.TrainNoteFragment
import com.example.fitnessapp.viewModels.ScheduleViewModel

@SuppressLint("NotifyDataSetChanged")
class TrainNotesAdapter(
    val viewmodel: ScheduleViewModel,
    lifecycleOwner: LifecycleOwner,
    val fragment: Fragment
) :
    RecyclerView.Adapter<TrainNotesAdapter.TrainNotesHolder>() {

    init {
        viewmodel.trainNotes.observe(lifecycleOwner) {
            notifyDataSetChanged()
        }
    }

    class TrainNotesHolder(val binding: ScheduleNoteCardBinding) :
        RecyclerView.ViewHolder(binding.root)

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
        if (viewmodel.trainNotes.value != null) {
            val trainNote = viewmodel.trainNotes.value!![position]
            holder.binding.trainNote = trainNote
            if (trainNote.isCompleted)
                holder.binding.status.text = "Выполнена"
            else
                holder.binding.status.text = "Не выполнена"
            holder.binding.trainNoteCard.setOnClickListener {
                fragment.childFragmentManager.beginTransaction()
                    .add(
                        R.id.fragment_container,
                        TrainNoteFragment.newInstance(trainNote.date, trainNote.id.toString())
                    )
                    .addToBackStack("TrainNoteFragment")
                    .commit()
            }
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.trainNotes.value?.size ?: 0
    }
}
