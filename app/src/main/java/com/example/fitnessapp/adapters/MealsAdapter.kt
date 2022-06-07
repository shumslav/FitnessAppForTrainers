package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.GroupMusclesCardBinding
import com.example.fitnessapp.databinding.MealCardBinding
import com.example.fitnessapp.models.SomeMeal
import com.example.fitnessapp.viewModels.ExercisesViewModel
import com.example.fitnessapp.viewModels.MealsViewModel

@SuppressLint("NotifyDataSetChanged")
class MealsAdapter(val viewModel: MealsViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<MealsAdapter.MealsHolder>() {

    var sortedMeals = mutableListOf<SomeMeal>()

    init{
        viewModel.selectedMeals.observe(lifecycleOwner){
            Log.i("UNSORTED", it.toString())
            sortedMeals = viewModel.sortMeals(it)
            Log.i("SORTED", sortedMeals.toString())
            notifyDataSetChanged()
        }
    }

    class MealsHolder(val binding: MealCardBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsHolder {
        return MealsHolder(
            MealCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MealsHolder, position: Int) {
        holder.binding.meal = sortedMeals[position]
        val numberMeal = "Прием пищи ${position+1}"
        holder.binding.numberMeal.text = numberMeal
        holder.binding.removeMeal.setOnClickListener {
            val copyMeals = mutableListOf<SomeMeal>()
            copyMeals.addAll(sortedMeals)
            copyMeals.removeAt(position)
            viewModel.selectedMeals.value = copyMeals
        }
    }

    override fun getItemCount(): Int {
        return sortedMeals.size
    }
}