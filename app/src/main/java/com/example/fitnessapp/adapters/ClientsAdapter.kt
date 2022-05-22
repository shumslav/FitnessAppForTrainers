package com.example.fitnessapp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.AddedExerciseCardBinding
import com.example.fitnessapp.databinding.ClientCardBinding
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.ui.MainActivity
import com.example.fitnessapp.viewModels.ClientsViewModel
import com.example.fitnessapp.viewModels.NewNoteViewModel
import com.google.firebase.ktx.Firebase

@SuppressLint("NotifyDataSetChanged")
class ClientsAdapter(val viewmodel: ClientsViewModel, lifecycleOwner: LifecycleOwner, val activity: Activity):
    RecyclerView.Adapter<ClientsAdapter.ClientsHolder>() {

    val currentClient = CurrentClient(activity)

    init {
        viewmodel.clients.observe(lifecycleOwner){
            notifyDataSetChanged()
        }
    }

    class ClientsHolder(val binding: ClientCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientsHolder {
        return ClientsHolder(
            ClientCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ClientsHolder, position: Int) {
        if (!viewmodel.clients.value.isNullOrEmpty()) {
            val client = viewmodel.clients.value!![position]
            holder.binding.client = client
            holder.binding.card.setOnClickListener {
                currentClient.login = client.login
                currentClient.name = client.name
                activity.startActivity(
                    Intent(activity,MainActivity::class.java))
            }
        }

    }

    override fun getItemCount(): Int {
        return viewmodel.clients.value?.size ?: 0
    }
}
