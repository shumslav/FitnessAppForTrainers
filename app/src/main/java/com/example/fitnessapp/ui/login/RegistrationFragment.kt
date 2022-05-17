package com.example.fitnessapp.ui.login


import NODE_TRAINERS
import NODE_USERS
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnessapp.databinding.FragmentRegistrationBinding
import com.example.fitnessapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast


class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        binding.fragment = this

//        binding.image.setOnClickListener {
//            val photoPickerIntent = Intent(Intent.ACTION_PICK)
//            photoPickerIntent.type = "image/*"
//            requireActivity().startActivityForResult(photoPickerIntent, GALLERY_REQUEST)
//        }

        return binding.root
    }

    private fun checkEmptyFields(): Boolean {
        return (binding.login.text.toString() != ""
                && binding.password.text.toString() != ""
                && binding.name.text.toString() != "")
    }

    fun registration() {
        if (checkEmptyFields()) {
            val login = binding.login.text.toString()
            val password = binding.password.text.toString()
            val name = binding.name.text.toString()
            Firebase.database.reference.child(NODE_TRAINERS).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.hasChild(login)) {
                            Firebase.database.reference.child(NODE_USERS)
                                .addListenerForSingleValueEvent(
                                    object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (!snapshot.hasChild(login)) {
                                                Firebase.database.reference
                                                    .child(NODE_TRAINERS)
                                                    .child(login)
                                                    .setValue(User(login, password, name))
                                                this@RegistrationFragment.requireActivity().supportFragmentManager.popBackStack()
                                            } else
                                                makeToast(
                                                    requireContext(),
                                                    "Такой пользователь уже есть"
                                                )
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                        } else makeToast(requireContext(), "Такой пользователь уже есть")
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        } else {
            makeToast(requireContext(), "Заполните все поля")
        }
    }
}