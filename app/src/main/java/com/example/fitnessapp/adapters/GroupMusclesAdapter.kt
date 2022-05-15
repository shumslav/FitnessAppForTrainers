package com.example.fitnessapp.adapters

import NODE_GROUP_MUSCLES
import NODE_USERS
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.AddedExerciseCardBinding
import com.example.fitnessapp.databinding.GroupMusclesCardBinding
import com.example.fitnessapp.viewModels.GroupMusclesViewModel
import com.example.fitnessapp.viewModels.NewNoteViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@SuppressLint("NotifyDataSetChanged")
class GroupMusclesAdapter(val viewModel:GroupMusclesViewModel, lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<GroupMusclesAdapter.GroupMusclesHolder>() {

    init{
        viewModel.groupMuscles.observe(lifecycleOwner){
            notifyDataSetChanged()
        }
    }

    class GroupMusclesHolder(val binding: GroupMusclesCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMusclesHolder {
        return GroupMusclesHolder(
            GroupMusclesCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupMusclesHolder, position: Int) {
        if (!viewModel.groupMuscles.value.isNullOrEmpty()){
            holder.binding.name = viewModel.groupMuscles.value!![position]
            holder.binding.count = "${position+1}."
            holder.binding.remove.setOnClickListener {
                deleteGroupMuscles(viewModel.groupMuscles.value!![position])
            }
        }
    }

    override fun getItemCount(): Int {
        return viewModel.groupMuscles.value?.size ?: 0
    }

    fun deleteGroupMuscles(groupMuscles: String) {
        Firebase.database.reference
            .child(NODE_USERS)
            .child(viewModel.user.login)
            .child(NODE_GROUP_MUSCLES)
            .child(groupMuscles)
            .removeValue()
    }

}