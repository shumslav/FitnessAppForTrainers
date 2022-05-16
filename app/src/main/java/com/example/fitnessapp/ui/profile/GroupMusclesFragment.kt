package com.example.fitnessapp.ui.profile

import NODE_GROUP_MUSCLES
import NODE_USERS
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.adapters.GroupMusclesAdapter
import com.example.fitnessapp.databinding.FragmentGroupMusclesBinding
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.viewModels.GroupMusclesViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast

class GroupMusclesFragment : Fragment() {

    lateinit var viewModel:GroupMusclesViewModel
    lateinit var client: CurrentClient
    lateinit var binding: FragmentGroupMusclesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupMusclesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[GroupMusclesViewModel::class.java]
        client = CurrentClient(requireContext())
        val adapter = GroupMusclesAdapter(viewModel,this)

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.fragment = this

        viewModel.isAddingNewGroup.observe(viewLifecycleOwner) {
            if (it) {
                binding.addGroup.visibility = View.GONE
                binding.formNewGroup.visibility = View.VISIBLE
            } else {
                binding.formNewGroup.visibility = View.GONE
                binding.addGroup.visibility = View.VISIBLE
                binding.newGroup.text.clear()
            }
        }

        return binding.root
    }

    fun addNewGroup(){
        viewModel.isAddingNewGroup.value = true
    }

    fun acceptNewGroup(){
        val newGroup = binding.newGroup.text.toString()
        if ( newGroup != ""){
            val ref =Firebase.database.reference
                .child(NODE_USERS)
                .child(client.login)
                .child(NODE_GROUP_MUSCLES)

            ref.addListenerForSingleValueEvent(
                    object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.hasChild(newGroup)){
                                ref.child(newGroup).setValue(newGroup)
                            }
                            else
                                makeToast(requireContext(),"Такая группа уже есть")
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    }
                )
            viewModel.isAddingNewGroup.value = false
        }
        else{
            makeToast(requireContext(), "Название пустое")
        }

    }
    fun cancelAddNewGroup(){
        viewModel.isAddingNewGroup.value = false
    }
}