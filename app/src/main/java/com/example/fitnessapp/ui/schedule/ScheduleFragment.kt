package com.example.fitnessapp.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentScheduleBinding
import makeToast

class ScheduleFragment : Fragment() {

//    lateinit var viewModel:ScheduleViewModel
    private val viewModel: ScheduleViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleBinding.inflate(inflater,container,false)
//        viewModel = ViewModelProvider(this).get(ScheduleViewModel::class.java)
        return binding.root
    }
}