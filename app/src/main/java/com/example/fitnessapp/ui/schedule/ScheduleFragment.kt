package com.example.fitnessapp.ui.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.FragmentScheduleBinding
import com.example.fitnessapp.ui.schedule.adapters.DaysAdapter
import makeToast

class ScheduleFragment : Fragment() {

    private val viewModel: ScheduleViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScheduleBinding.inflate(inflater,container,false)
        val daysAdapter = DaysAdapter(viewModel, this)
        binding.recyclerDays.adapter= daysAdapter
        binding.recyclerDays.layoutManager = LinearLayoutManager(requireContext())
        viewModel.isAddNoticeVisible.observe(viewLifecycleOwner) { t ->
            if (t == true)
                binding.addNotice.visibility = View.VISIBLE
            else
                binding.addNotice.visibility = View.GONE
        }
        viewModel.lastPickedDay.observe(viewLifecycleOwner) {
            viewModel.getTrainNotes()
        }
        return binding.root
    }
}