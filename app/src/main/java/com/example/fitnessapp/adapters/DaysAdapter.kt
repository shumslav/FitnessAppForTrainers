package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.ScheduleDayCardBinding
import com.example.fitnessapp.viewModels.ScheduleViewModel
import com.example.fitnessapp.models.CalendarDay
import com.google.android.material.card.MaterialCardView

@SuppressLint("NotifyDataSetChanged")
class DaysAdapter(val viewmodel: ScheduleViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<DaysAdapter.DayCardHolder>() {

    private var lastPosition: MaterialCardView? = null
    private var lastCalendarDay: CalendarDay? = null
    var id = -1

    init {
        viewmodel.datesData.observe(
            lifecycleOwner
        ) { t ->
            Log.i("MyError", "UpdateDates")
            lastPosition = null
            lastCalendarDay = null
            this@DaysAdapter.notifyDataSetChanged()
        }
    }

    class DayCardHolder(val binding: ScheduleDayCardBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.cardDay.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayCardHolder {
        return DayCardHolder(
            ScheduleDayCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DayCardHolder, position: Int) {
        if (!viewmodel.datesData.value.isNullOrEmpty()) {
            val datesValue = viewmodel.datesData.value!!
            val calendarDay = datesValue[position]
            holder.binding.calendarDay = calendarDay

            holder.binding.cardDay.setOnClickListener {
                viewmodel.isAddNoticeVisible.value = true
                viewmodel.lastPickedDay.value = calendarDay
                if (id != -1){
                    notifyItemChanged(id)
                }
                id = holder.adapterPosition
                notifyItemChanged(holder.adapterPosition)

            }
            if (calendarDay.dateString == viewmodel.lastPickedDay.value?.dateString) {
                holder.binding.cardDay.setCardBackgroundColor(Color.rgb(100, 89, 76))
                id = holder.adapterPosition
            }
            else
                holder.binding.cardDay.setCardBackgroundColor(Color.rgb(164,143,116))
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.datesData.value?.size ?: 0
    }
}