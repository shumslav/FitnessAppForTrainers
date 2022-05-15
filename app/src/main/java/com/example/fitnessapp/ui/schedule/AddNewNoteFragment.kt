package com.example.fitnessapp.ui.schedule

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.databinding.FragmentAddNewNoteBinding
import com.example.fitnessapp.models.AddedExercise
import com.example.fitnessapp.adapters.AddedExercisesAdapter
import com.example.fitnessapp.viewModels.NewNoteViewModel
import java.util.*


class AddNewNoteFragment : Fragment() {

    lateinit var viewModel: NewNoteViewModel
    lateinit var startTimeListener: OnTimeSetListener
    lateinit var endTimeListener: OnTimeSetListener
    lateinit var binding: FragmentAddNewNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NewNoteViewModel::class.java]
        val exercisesAdapter = AddedExercisesAdapter(viewModel, this)
        binding.recyclerAddedExercises.adapter = exercisesAdapter
        binding.recyclerAddedExercises.layoutManager = LinearLayoutManager(requireContext())

        binding.fragment = this
        binding.viewmodel = viewModel

        startTimeListener = OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.calendarStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.calendarStart.set(Calendar.MINUTE, minute)
            viewModel.startTime = viewModel.getTime(viewModel.calendarStart)
            binding.timeStart.text = viewModel.startTime
        }

        endTimeListener = OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.calendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.calendarEnd.set(Calendar.MINUTE, minute)
            viewModel.endTime = viewModel.getTime(viewModel.calendarEnd)
            binding.timeEnd.text = viewModel.endTime
        }

        viewModel.isAddingNewExercise.observe(viewLifecycleOwner) {
            if (it) {
                binding.name.setText("")
                binding.rounds.setText("")
                binding.repetitions.setText("")
                binding.weight.setText("")
                binding.addNewExercise.visibility = View.GONE
                binding.addExerciseForm.visibility = View.VISIBLE
            } else {
                binding.addNewExercise.visibility = View.VISIBLE
                binding.addExerciseForm.visibility = View.GONE
            }
        }

        return binding.root
    }

    fun setTimeStart() {
        TimePickerDialog(
            requireContext(), startTimeListener,
            viewModel.calendarStart.get(Calendar.HOUR_OF_DAY),
            viewModel.calendarStart.get(Calendar.MINUTE),
            true
        )
            .show()
    }

    fun setTimeEnd() {
        TimePickerDialog(
            requireContext(), endTimeListener,
            viewModel.calendarEnd.get(Calendar.HOUR_OF_DAY),
            viewModel.calendarEnd.get(Calendar.MINUTE),
            true
        )
            .show()
    }

    fun addExercise() {
        val exercise = AddedExercise(
            binding.name.text.toString(),
            binding.repetitions.text.toString(),
            binding.rounds.text.toString(),
            binding.weight.text.toString()
        )
        val exercises = mutableListOf<AddedExercise>()
        if (!viewModel.addedExercises.value.isNullOrEmpty())
            exercises.addAll(viewModel.addedExercises.value!!)
        exercises.add(exercise)
        viewModel.addedExercises.value = exercises
        viewModel.isAddingNewExercise.value = false
        Log.i("TAG", binding.name.text.toString())
    }
}