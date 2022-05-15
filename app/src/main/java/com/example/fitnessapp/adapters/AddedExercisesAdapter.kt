package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.AddedExerciseCardBinding
import com.example.fitnessapp.viewModels.NewNoteViewModel

@SuppressLint("NotifyDataSetChanged")
class AddedExercisesAdapter(val viewmodel: NewNoteViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<AddedExercisesAdapter.AddedExercisesHolder>() {

    init {
        viewmodel.addedExercises.observe(
            lifecycleOwner
        ) { t ->
            Log.i("MyError", "UpdateDates")
            this.notifyDataSetChanged()
        }
    }

    class AddedExercisesHolder(val binding: AddedExerciseCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedExercisesHolder {
        return AddedExercisesHolder(
            AddedExerciseCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddedExercisesHolder, position: Int) {
        if (!viewmodel.addedExercises.value.isNullOrEmpty()) {
            val addedExercise = viewmodel.addedExercises.value!![position]
            holder.binding.addedExercise = addedExercise
        }

    }

    override fun getItemCount(): Int {
        return viewmodel.addedExercises.value?.size ?: 0
    }
}