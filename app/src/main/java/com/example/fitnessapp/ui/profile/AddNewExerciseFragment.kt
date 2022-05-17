package com.example.fitnessapp.ui.profile

import NODE_EXERCISES
import NODE_GROUP_MUSCLES
import NODE_USERS
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.databinding.FragmentAddNewExerciseBinding
import com.example.fitnessapp.enums.Calculation
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.Exercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast


class AddNewExerciseFragment : Fragment() {

    lateinit var client: CurrentClient
    lateinit var binding: FragmentAddNewExerciseBinding
    var group: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewExerciseBinding.inflate(inflater, container, false)
        client = CurrentClient(requireContext())
        val groups: MutableLiveData<MutableList<String>> = MutableLiveData()
        Firebase.database.reference
            .child(NODE_USERS)
            .child(client.login)
            .child(NODE_GROUP_MUSCLES).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val result = mutableListOf<String>()
                        snapshot.children.forEach {
                            if (it.getValue(String::class.java) != null)
                                result.add(it.getValue(String::class.java)!!)
                        }
                        groups.value = result
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        groups.observe(viewLifecycleOwner) {
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it)
            binding.group.adapter = adapter
        }
        binding.isWeight.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Есть", "Нету")
            )

        binding.repetitions.adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Кг", "Минуты")
            )

        binding.addNewTrain.setOnClickListener {
            if (checkIsEmpty()) {
                makeToast(requireContext(), "Заполните все поля")
                return@setOnClickListener
            }
            val name = binding.name.text.toString()
            val group = binding.group.selectedItem as String
            val rep = if (binding.repetitions.selectedItem as String == "Минуты")
                Calculation.SECONDS
            else
                Calculation.REPETITIONS
            val isHaveWeight = binding.isWeight.selectedItem as String != "Нету"
            val exercise = Exercise(group, name, rep, isHaveWeight)
            val ref = Firebase.database.reference.child(NODE_USERS).child(client.login).child(NODE_EXERCISES)
                .child(exercise.bodyPart)
            ref.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(exercise.name))
                            makeToast(requireContext(), "Имя занято")
                        else {
                            ref.child(exercise.name).setValue(exercise)
                            requireActivity().onBackPressed()
                        } }
                    override fun onCancelled(error: DatabaseError) {} })
        }
        return binding.root
    }

    fun checkIsEmpty(): Boolean {
        val name = binding.name.text.toString()
        val group = binding.group.selectedItem as String?
        return !(name.isNotEmpty() && !group.isNullOrEmpty())
    }
}