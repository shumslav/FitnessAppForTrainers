package com.example.fitnessapp.ui.chat

import NODE_MESSAGES
import NODE_USERS
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.MessagesAdapter
import com.example.fitnessapp.databinding.FragmentChatBinding
import com.example.fitnessapp.databinding.FragmentMealsBinding
import com.example.fitnessapp.models.Message
import com.example.fitnessapp.viewModels.ChatViewModel
import com.example.fitnessapp.viewModels.MealsViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast
import java.util.*

class ChatFragment : Fragment() {

    lateinit var viewModel: ChatViewModel
    lateinit var binding: FragmentChatBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.name.text = viewModel.currentClient.name

        binding.recyclerMessages.adapter = MessagesAdapter(viewModel, viewLifecycleOwner)
        binding.recyclerMessages.layoutManager = LinearLayoutManager(requireContext())

        binding.send.setOnClickListener {
            viewModel.sendMessage(binding.message.text.toString())
            binding.message.text.clear()
    }

    return binding.root
}
}