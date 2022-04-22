package com.example.fitnessapp.ui.schedule.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.ScheduleDayCardBinding
import com.example.fitnessapp.ui.schedule.Models.CalendarDay
import com.example.fitnessapp.ui.schedule.ScheduleViewModel

class DaysAdapter(val viewmodel:ScheduleViewModel, lifecycleOwner: LifecycleOwner): RecyclerView.Adapter<DaysAdapter.DayCardHolder>() {

    init {
        viewmodel.datesData.observe(lifecycleOwner, object: Observer<MutableList<CalendarDay>>{
            override fun onChanged(t: MutableList<CalendarDay>?) {
                this@DaysAdapter.notifyItemRangeChanged(0,t?.size ?: 0)
            }
        })
    }

    class DayCardHolder(val binding:ScheduleDayCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayCardHolder {
        return DayCardHolder(ScheduleDayCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: DayCardHolder, position: Int) {
        if (!viewmodel.datesData.value.isNullOrEmpty()) {
            val datesValue = viewmodel.datesData.value!!
            holder.binding.day.text = datesValue[position].day
            holder.binding.month.text = datesValue[position].month
            holder.binding.cardDay.setOnClickListener {
                viewmodel.isAddNoticeVisible.value = true
            }
        }
        else{
            Log.i("Here", "HEre")
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.datesData.value?.size ?: 0
    }
}