package com.example.fitnessapp.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentProfileBinding
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.CurrentUser
import com.example.fitnessapp.ui.login.ChooseClientActivity
import com.example.fitnessapp.ui.login.EnterActivity


class ProfileFragment : Fragment() {

    lateinit var client: CurrentClient
    lateinit var user: CurrentUser
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        client = CurrentClient(requireContext())
        user = CurrentUser(requireContext())

        binding.fragment = this

        return binding.root
    }

    fun logout() {
        client.logout()
        user.logout()
        requireActivity().startActivity(Intent(requireContext(), EnterActivity::class.java))
        requireActivity().finish()
    }

    fun clients(){
        requireActivity().startActivity(Intent(requireContext(), ChooseClientActivity::class.java))
        requireActivity().finish()
    }

    fun groupMuscles() {
        childFragmentManager.beginTransaction()
            .add(R.id.container_fragment_profile, GroupMusclesFragment())
            .addToBackStack("group_muscles")
            .commit()
    }

    fun exercises() {
        childFragmentManager.beginTransaction()
            .add(R.id.container_fragment_profile, ExercisesFragment())
            .addToBackStack("exercises")
            .commit()
    }
}