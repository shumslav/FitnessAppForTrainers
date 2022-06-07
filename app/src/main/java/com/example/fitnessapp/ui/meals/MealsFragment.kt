package com.example.fitnessapp.ui.meals

import NODE_MEALS
import NODE_USERS
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
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
import com.example.fitnessapp.adapters.MealsAdapter
import com.example.fitnessapp.databinding.FragmentAddNewExerciseBinding
import com.example.fitnessapp.databinding.FragmentMealsBinding
import com.example.fitnessapp.models.Meal
import com.example.fitnessapp.models.SomeMeal
import com.example.fitnessapp.viewModels.ExercisesViewModel
import com.example.fitnessapp.viewModels.MealsViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast
import java.util.*

class MealsFragment : Fragment() {

    lateinit var binding: FragmentMealsBinding
    lateinit var viewModel: MealsViewModel
    lateinit var timeListener: TimePickerDialog.OnTimeSetListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MealsViewModel::class.java]

        binding.meals.adapter = MealsAdapter(viewModel, viewLifecycleOwner)
        binding.meals.layoutManager = LinearLayoutManager(requireContext())

        timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            setTime(hourOfDay.toString(), minute.toString())
        }

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

        binding.addMeal.setOnClickListener {
            binding.name.setText("")
            binding.time.setText("")
            binding.planMeal.setText("")
            binding.addMeal.visibility = View.GONE
            binding.newMealForm.visibility = View.VISIBLE
        }

        binding.cancel.setOnClickListener {
            binding.name.setText("")
            binding.time.setText("")
            binding.planMeal.setText("")
            binding.newMealForm.visibility = View.GONE
            binding.addMeal.visibility = View.VISIBLE
        }

        binding.accept.setOnClickListener {
            if (checkEmptyFields()) {
                val copyMeals = mutableListOf<SomeMeal>()
                if (viewModel.selectedMeals.value != null)
                    copyMeals.addAll(viewModel.selectedMeals.value!!)
                copyMeals.add(
                    SomeMeal(
                        binding.name.text.toString(),
                        binding.time.text.toString(),
                        binding.planMeal.text.toString()
                    )
                )
                viewModel.selectedMeals.value = copyMeals
                binding.newMealForm.visibility = View.GONE
                binding.addMeal.visibility = View.VISIBLE
            }
            else{
                makeToast(requireContext(),"Заполните все поля")
            }
        }

        binding.time.setOnClickListener {
            setTimeMeal()
        }

        binding.addDay.setOnClickListener {
            val meal = Meal()
            meal.weekDay = binding.dayOfWeek.selectedItem as String
            meal.fats = binding.fats.text.toString()
            meal.calories = binding.calories.text.toString()
            meal.protein = binding.protein.text.toString()
            meal.carbohydrates = binding.carbohydrates.text.toString()
            meal.notes = binding.notes.text.toString()
            meal.meals = viewModel.selectedMeals.value ?: mutableListOf()

            Firebase.database.reference.child(NODE_USERS)
                .child(viewModel.user.login)
                .child(NODE_MEALS)
                .child(meal.weekDay)
                .setValue(meal).addOnSuccessListener {
                    makeToast(requireContext(), "Данные сохранены")
                }
        }

        return binding.root
    }

    private fun checkEmptyFields(): Boolean {
        return (binding.name.text.isNotEmpty() && binding.time.text.isNotEmpty() && binding.planMeal.text.isNotEmpty())
    }

    fun setMeal(meal: Meal) {
        binding.calories.setText(meal.calories)
        binding.protein.setText(meal.protein)
        binding.fats.setText(meal.fats)
        binding.carbohydrates.setText(meal.carbohydrates)
        binding.notes.setText(meal.notes)
        viewModel.selectedMeals.value = meal.meals
        Log.i("MEALS", meal.meals.toString())
    }

    private fun setTimeMeal() {
        TimePickerDialog(
            requireContext(), timeListener,
            viewModel.calendar.get(Calendar.HOUR_OF_DAY),
            viewModel.calendar.get(Calendar.MINUTE),
            true
        )
            .show()
    }

    private fun setTime(hour: String, minute: String) {
        val time =
            if (hour.length == 1)
                if (minute.length == 1)
                    "0${hour}:0${minute}"
                else
                    "0${hour}:${minute}"
            else
                if (minute.length == 1)
                    "${hour}:0${minute}"
                else
                    "${hour}:${minute}"
        binding.time.setText(time)
    }

}