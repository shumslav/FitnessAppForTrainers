package com.example.fitnessapp.ui.meals

import NODE_MEALS
import NODE_USERS
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentAddNewExerciseBinding
import com.example.fitnessapp.databinding.FragmentMealsBinding
import com.example.fitnessapp.models.Meal
import com.example.fitnessapp.viewModels.ExercisesViewModel
import com.example.fitnessapp.viewModels.MealsViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast

class MealsFragment : Fragment() {

    lateinit var binding: FragmentMealsBinding
    lateinit var viewModel: MealsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MealsViewModel::class.java]

        binding.dayOfWeek.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            viewModel.daysOfWeek
        )

        viewModel.meals.observe(viewLifecycleOwner) {
            setMeal(viewModel.meals.value!![binding.dayOfWeek.selectedItem as String]!!)
        }

        binding.dayOfWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val day = viewModel.daysOfWeek[p2]
                if (viewModel.meals.value != null)
                    setMeal(viewModel.meals.value!![day]!!)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.addDay.setOnClickListener {
            val meal = Meal()
            meal.weekDay = binding.dayOfWeek.selectedItem as String
            meal.fats = binding.fats.text.toString()
            meal.calories = binding.calories.text.toString()
            meal.protein = binding.protein.text.toString()
            meal.carbohydrates = binding.carbohydrates.text.toString()
            meal.notes = binding.notes.text.toString()

            Firebase.database.reference.child(NODE_USERS)
                .child(viewModel.user.login)
                .child(NODE_MEALS)
                .child(meal.weekDay)
                .setValue(meal).addOnSuccessListener {
                    makeToast(requireContext(),"Данные сохранены") }
        }

        return binding.root
    }

    fun setMeal(meal: Meal) {
        binding.calories.setText(meal.calories)
        binding.protein.setText(meal.protein)
        binding.fats.setText(meal.fats)
        binding.carbohydrates.setText(meal.carbohydrates)
        binding.notes.setText(meal.notes)
    }
}