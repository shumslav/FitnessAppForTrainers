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

        binding.recyclerMessages.adapter = MessagesAdapter(viewModel,viewLifecycleOwner)
        binding.recyclerMessages.layoutManager = LinearLayoutManager(requireContext())

        binding.send.setOnClickListener {
            makeToast(requireContext(),"HEre")
            sendMessage()
        }

        return binding.root
    }

    fun sendMessage() {
        if (binding.message.text.toString().isEmpty())
            return
        makeToast(requireContext(), viewModel.currentUser.login)
        val calendar = Calendar.getInstance()
        val time = getTime(calendar)
        val date = getDate(calendar)
        val message =
            Message(binding.message.text.toString(), viewModel.currentUser.login, time, date)

        Firebase.database.reference.child(NODE_USERS).child(viewModel.currentClient.login)
            .child(NODE_MESSAGES).push().setValue(message)

        binding.message.text.clear()
    }

    private fun getDate(calendar: Calendar): String {
        val day = if ((calendar.get(Calendar.DAY_OF_MONTH)).toString().length == 1)
            "0${calendar.get(Calendar.DAY_OF_MONTH)}"
        else
            "${calendar.get(Calendar.DAY_OF_MONTH)}"

        val month = if ((calendar.get(Calendar.MONTH)+1).toString().length == 1)
            "0${calendar.get(Calendar.MONTH)+1}"
        else
            "${calendar.get(Calendar.MONTH)+1}"

        val year = if (calendar.get(Calendar.YEAR).toString().length == 1)
            "0${calendar.get(Calendar.YEAR)}"
        else
            "${calendar.get(Calendar.YEAR)}"
        return "${day}:${month}:${year}"
    }

    fun getTime(calendar: Calendar): String {
        val hours = if (calendar.get(Calendar.HOUR_OF_DAY).toString().length == 1)
            "0${calendar.get(Calendar.HOUR_OF_DAY)}"
        else
            "${calendar.get(Calendar.HOUR_OF_DAY)}"

        val minutes = if (calendar.get(Calendar.MINUTE).toString().length == 1)
            "0${calendar.get(Calendar.MINUTE)}"
        else
            "${calendar.get(Calendar.MINUTE)}"

        return "${hours}:${minutes}"
    }
}