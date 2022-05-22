package com.example.fitnessapp.ui.schedule

import NODE_TRAIN_NOTES
import NODE_USERS
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.TrainNoteExercisesAdapter
import com.example.fitnessapp.databinding.FragmentScheduleBinding
import com.example.fitnessapp.databinding.FragmentTrainNoteBinding
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.TrainNote
import com.example.fitnessapp.viewModels.ScheduleViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val DATE = "Date"
private const val Id = "Id"

class TrainNoteFragment : Fragment() {
    private var date: String? = null
    private var id: String? = null
    lateinit var binding: FragmentTrainNoteBinding
    lateinit var client: CurrentClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString(DATE)
            id = it.getString(Id)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainNoteBinding.inflate(inflater, container, false)
        client = CurrentClient(requireContext())
        val viewModel = ViewModelProvider(requireActivity())[ScheduleViewModel::class.java]
        if (date == null || id == null)
            requireActivity().onBackPressed()
        else {
            val trainNote: MutableLiveData<TrainNote> = MutableLiveData()
            binding.exercisesRecycler.adapter = TrainNoteExercisesAdapter(trainNote)
            binding.exercisesRecycler.layoutManager = LinearLayoutManager(requireContext())
            Firebase.database.reference.child(NODE_USERS).child(client.login)
                .child(NODE_TRAIN_NOTES)
                .child(date!!)
                .child(id!!).addValueEventListener(
                    object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val currentTrainNote = snapshot.getValue(TrainNote::class.java)
                            if (currentTrainNote!=null)
                                trainNote.value = currentTrainNote
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })
            trainNote.observe(viewLifecycleOwner){
                binding.trainNote = it
                if (it.notes!=""){
                    binding.notes.text = it.notes
                    binding.notesForm.visibility = View.VISIBLE
                }
                else{
                    binding.notesForm.visibility = View.GONE
                }
                binding.exercisesRecycler.adapter?.notifyDataSetChanged()
            }
            binding.delete.setOnClickListener {
                Firebase.database.reference.child(NODE_USERS)
                    .child(client.login)
                    .child(NODE_TRAIN_NOTES)
                    .child(date!!)
                    .child(id!!).removeValue()
                viewModel.getTrainNotes()
                requireActivity().onBackPressed()
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(date: String, id: String) =
            TrainNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(DATE, date)
                    putString(Id, id)
                }
            }
    }
}