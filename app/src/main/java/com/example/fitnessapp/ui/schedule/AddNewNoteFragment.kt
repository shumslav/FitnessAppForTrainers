package com.example.fitnessapp.ui.schedule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R


class AddNewNoteFragment : Fragment() {

    lateinit var viewModel: ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        Log.i("Test", viewModel.datesData.value.toString())
        return inflater.inflate(R.layout.fragment_add_new_note, container, false)
    }
}