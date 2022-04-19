package com.example.fitnessapp.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R
import makeToast

class ScheduleFragment : Fragment() {

    lateinit var viewModel:ScheduleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_schedule, container, false)
        viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        return root
    }
}