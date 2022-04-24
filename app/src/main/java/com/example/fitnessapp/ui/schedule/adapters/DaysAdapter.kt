package com.example.fitnessapp.ui.schedule.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.ScheduleDayCardBinding
import com.example.fitnessapp.ui.schedule.ScheduleViewModel
import com.example.fitnessapp.models.CalendarDay
import com.google.android.material.card.MaterialCardView

@SuppressLint("NotifyDataSetChanged")
class DaysAdapter(val viewmodel: ScheduleViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<DaysAdapter.DayCardHolder>() {

    private var lastPosition: MaterialCardView? = null

    init {
        viewmodel.datesData.observe(
            lifecycleOwner
        ) { t ->
            Log.i("MyError", "UpdateDates")
            lastPosition = null
            this@DaysAdapter.notifyDataSetChanged()
        }
    }

    class DayCardHolder(val binding: ScheduleDayCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayCardHolder {
        return DayCardHolder(
            ScheduleDayCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DayCardHolder, position: Int) {
        if (!viewmodel.datesData.value.isNullOrEmpty()) {
            val datesValue = viewmodel.datesData.value!!
            val calendarDay = datesValue[position]
            holder.binding.calendarDay = calendarDay
            holder.binding.cardDay.setOnClickListener { onClickDay(holder, calendarDay) }
            if (calendarDay == viewmodel.lastPickedDay.value) {
                onClickDay(holder, calendarDay)
            }
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.datesData.value?.size ?: 0
    }

    private fun onClickDay(holder: DayCardHolder, calendarDay: CalendarDay) {
        viewmodel.isAddNoticeVisible.value = true
        if (lastPosition != null) {
            lastPosition!!.setCardBackgroundColor(Color.WHITE)
        }
        holder.binding.cardDay.setCardBackgroundColor(Color.rgb(157, 157, 157))
        lastPosition = holder.binding.cardDay
        viewmodel.lastPickedDay.value = calendarDay
    }
}