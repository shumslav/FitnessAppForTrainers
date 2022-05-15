package com.example.fitnessapp.ui.login

import NODE_EXERCISES
import NODE_GROUP_MUSCLES
import NODE_PASSWORD
import NODE_PASSWORDS
import NODE_USERS
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentRegistrationBinding
import com.example.fitnessapp.databinding.FragmentScheduleBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast
import standartExercises
import standartGroupMuscles
import kotlin.math.log


class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.fragment = this

        return binding.root
    }

    private fun checkEmptyFields(): Boolean {
        return (binding.login.text.toString() != "" && binding.password.text.toString() != "")
    }

    fun registration() {
        if (checkEmptyFields()) {
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            makeToast(requireContext(), "Такой пользователь уже есть")
            Firebase.database.reference.child(NODE_PASSWORDS).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.hasChild(login)) {
                            Firebase.database.reference
                                .child(NODE_PASSWORDS)
                                .child(login)
                                .child(NODE_PASSWORD)
                                .setValue(password)
                            addCommonExercisesAndGroups(login)
                            this@RegistrationFragment.requireActivity().supportFragmentManager.popBackStack()
                        }
                        else
                            makeToast(requireContext(), "Такой пользователь уже есть")
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        }
        else{
            makeToast(requireContext(),"Заполните все поля")
        }
    }

    private fun addCommonExercisesAndGroups(login:String){
        standartGroupMuscles.forEach {
            Firebase.database.reference
                .child(NODE_USERS)
                .child(login)
                .child(NODE_GROUP_MUSCLES)
                .child(it).setValue(it)
        }
        standartExercises.forEach {
            Firebase.database.reference
                .child(NODE_USERS)
                .child(login)
                .child(NODE_EXERCISES)
                .child(it.bodyPart)
                .child(it.name)
                .setValue(it)
        }
    }
}