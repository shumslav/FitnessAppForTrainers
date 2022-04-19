package com.example.fitnessapp.ui.schedule.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.ScheduleDayCardBinding
import com.example.fitnessapp.ui.schedule.Models.CalendarDay

class DaysAdapter(val dates:LiveData<List<CalendarDay>>): RecyclerView.Adapter<DaysAdapter.DayCardHolder>() {
    class DayCardHolder(val binding:ScheduleDayCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayCardHolder {
        return DayCardHolder(ScheduleDayCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DayCardHolder, position: Int) {
        if (!dates.value.isNullOrEmpty()) {
            val datesValue = dates.value!!
            holder.binding.day.text = datesValue[position].day.toString()
            holder.binding.month.text = datesValue[position].monthStr
        }
    }

    override fun getItemCount(): Int {
        return dates.value?.size ?: 0
    }
}