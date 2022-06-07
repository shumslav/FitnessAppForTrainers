package com.example.fitnessapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.ExercisesAdapter
import com.example.fitnessapp.databinding.FragmentExercisesBinding
import com.example.fitnessapp.models.Exercise
import com.example.fitnessapp.viewModels.ExercisesViewModel

class ExercisesFragment : Fragment() {

    lateinit var viewModel: ExercisesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentExercisesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ExercisesViewModel::class.java]

        binding.recycler.adapter = ExercisesAdapter(viewModel, viewLifecycleOwner)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.groupsMuscle.observe(viewLifecycleOwner) {
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it)
            binding.group.adapter = adapter
            if (it.isNotEmpty())
                binding.group.setSelection(0)
        }

        viewModel.groupExercises.observe(viewLifecycleOwner) {
            if (viewModel.selectedGroup.value != null) {
                val exercises = mutableListOf<Exercise>()
                if (it.containsKey(viewModel.selectedGroup.value)) {
                    it[viewModel.selectedGroup.value]!!.forEach {
                        exercises.add(it)
                    }
                    viewModel.selectedExercises.value = exercises
                }
            }
        }

        binding.group.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (viewModel.groupsMuscle.value != null) {
                    viewModel.selectedGroup.value = viewModel.groupsMuscle.value!![p2]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.addExercise.setOnClickListener {
            childFragmentManager.beginTransaction()
                .add(R.id.container_fragment_exercises, AddNewExerciseFragment())
                .addToBackStack("new_exercise")
                .commit()
        }

        viewModel.selectedGroup.observe(viewLifecycleOwner) {
            val exercises = mutableListOf<Exercise>()
            if (viewModel.groupExercises.value != null) {
                if (viewModel.groupExercises.value!!.containsKey(it)) {
                    viewModel.groupExercises.value!![it]!!.forEach {
                        exercises.add(it)
                    }
                    viewModel.selectedExercises.value = exercises
                }
            }
        }

        return binding.root
    }
}