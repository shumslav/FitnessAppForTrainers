package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.AddedExerciseCardBinding
import com.example.fitnessapp.databinding.MessageCardBinding
import com.example.fitnessapp.viewModels.ChatViewModel
import com.example.fitnessapp.viewModels.NewNoteViewModel

@SuppressLint("NotifyDataSetChanged")
class MessagesAdapter(val viewmodel: ChatViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<MessagesAdapter.MessagesHolder>() {

    var lastDate = ""

    init {
        viewmodel.messages.observe(lifecycleOwner){
            lastDate = ""
            this.notifyDataSetChanged()
        }
    }

    class MessagesHolder(val binding: MessageCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {
        return MessagesHolder(
            MessageCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {
        if (!viewmodel.messages.value.isNullOrEmpty()){
            val message = viewmodel.messages.value!![position]
            val isMe = (viewmodel.currentUser.login == message.owner)
            if (isMe){
                holder.binding.messageRight.text = message.text
                holder.binding.timeRight.text = message.time
                holder.binding.messagesFormRight.visibility = View.VISIBLE
            }
            else{
                holder.binding.messageLeft.text = message.text
                holder.binding.timeLeft.text = message.time
                holder.binding.messagesFormLeft.visibility = View.VISIBLE
            }
            if(lastDate!=message.date.replace(":",".")){
                holder.binding.date.text = message.date.replace(":",".")
                holder.binding.dateCard.visibility = View.VISIBLE
                lastDate = message.date.replace(":",".")
            }
        }
    }

    override fun getItemCount(): Int {
        return viewmodel.messages.value?.size ?: 0
    }
}