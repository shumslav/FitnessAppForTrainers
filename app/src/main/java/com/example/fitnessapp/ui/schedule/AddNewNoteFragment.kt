package com.example.fitnessapp.ui.schedule

import NODE_TRAIN_NOTES
import NODE_USERS
import android.R
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.databinding.FragmentAddNewNoteBinding
import com.example.fitnessapp.models.AddedExercise
import com.example.fitnessapp.adapters.AddedExercisesAdapter
import com.example.fitnessapp.enums.Calculation
import com.example.fitnessapp.models.Exercise
import com.example.fitnessapp.models.TrainNote
import com.example.fitnessapp.notifications.NotificationDate
import com.example.fitnessapp.notifications.PushNotification
import com.example.fitnessapp.viewModels.NewNoteViewModel
import com.example.fitnessapp.viewModels.ScheduleViewModel
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast
import java.util.*

private const val DATE = "Date"

class AddNewNoteFragment : Fragment() {

    lateinit var viewModel: NewNoteViewModel
    lateinit var startTimeListener: OnTimeSetListener
    lateinit var endTimeListener: OnTimeSetListener
    lateinit var binding: FragmentAddNewNoteBinding
    private var date: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            date = it.getString(DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NewNoteViewModel::class.java]
        val exercisesAdapter = AddedExercisesAdapter(viewModel, this)
        binding.recyclerAddedExercises.adapter = exercisesAdapter
        binding.recyclerAddedExercises.layoutManager = LinearLayoutManager(requireContext())

        binding.fragment = this
        binding.viewmodel = viewModel

        startTimeListener = OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.calendarStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.calendarStart.set(Calendar.MINUTE, minute)
            viewModel.startTime = viewModel.getTime(viewModel.calendarStart)
            binding.timeStart.text = viewModel.startTime
        }

        endTimeListener = OnTimeSetListener { view, hourOfDay, minute ->
            viewModel.calendarEnd.set(Calendar.HOUR_OF_DAY, hourOfDay)
            viewModel.calendarEnd.set(Calendar.MINUTE, minute)
            viewModel.endTime = viewModel.getTime(viewModel.calendarEnd)
            binding.timeEnd.text = viewModel.endTime
        }

        viewModel.isAddingNewExercise.observe(viewLifecycleOwner) {
            if (it) {
                binding.rounds.setText("")
                binding.repetitions.setText("")
                binding.weight.setText("")
                binding.addNewExercise.visibility = View.GONE
                binding.addExerciseForm.visibility = View.VISIBLE
            } else {
                binding.addNewExercise.visibility = View.VISIBLE
                binding.addExerciseForm.visibility = View.GONE
            }
        }

        viewModel.groupsMuscle.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, it)
            binding.groupsMuscles.adapter = adapter
        }

        viewModel.groupExercises.observe(viewLifecycleOwner) {
            if (viewModel.selectedGroup.value != null) {
                val exercises = mutableListOf<String>()
                if (it.containsKey(viewModel.selectedGroup.value)) {
                    it[viewModel.selectedGroup.value]!!.forEach {
                        exercises.add(it.name)
                    }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.simple_spinner_dropdown_item,
                        exercises
                    )
                    binding.name.adapter = adapter
                }
            }
        }

        viewModel.selectedExercise.observe(viewLifecycleOwner) {
            binding.weight.text.clear()
            binding.repetitions.text.clear()
            binding.rounds.text.clear()
            if (it.isHaveWeight) {
                binding.weight.visibility = View.VISIBLE
            } else {
                binding.weight.visibility = View.GONE
            }
            if (it.calculation == Calculation.REPETITIONS) {
                binding.repetitions.hint = "Повт."
            } else
                binding.repetitions.hint = "Мин."
        }

        viewModel.selectedGroup.observe(viewLifecycleOwner) {
            viewModel.addedExercises.value = mutableListOf()
            val exercises = mutableListOf<String>()
            if (viewModel.groupExercises.value != null) {
                if (viewModel.groupExercises.value!!.containsKey(it)) {
                    viewModel.groupExercises.value!![it]!!.forEach {
                        exercises.add(it.name)
                    }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.simple_spinner_dropdown_item,
                        exercises
                    )
                    binding.name.adapter = adapter
                }
            }
        }

        binding.groupsMuscles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (viewModel.groupsMuscle.value != null) {
                    viewModel.selectedGroup.value = viewModel.groupsMuscle.value!![p2]
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.name.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val exercise =
                    viewModel.groupExercises.value!![viewModel.selectedGroup.value!!]!![p2]
                viewModel.selectedExercise.value = exercise
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        return binding.root
    }

    fun setTimeStart() {
        TimePickerDialog(
            requireContext(), startTimeListener,
            viewModel.calendarStart.get(Calendar.HOUR_OF_DAY),
            viewModel.calendarStart.get(Calendar.MINUTE),
            true
        )
            .show()
    }

    fun setTimeEnd() {
        TimePickerDialog(
            requireContext(), endTimeListener,
            viewModel.calendarEnd.get(Calendar.HOUR_OF_DAY),
            viewModel.calendarEnd.get(Calendar.MINUTE),
            true
        )
            .show()
    }

    fun addExercise() {
        if (!checkFieldsEmpty() || viewModel.selectedExercise.value == null) {
            makeToast(requireContext(), "Заполните все поля")
            return
        }
        val selectedExercise = viewModel.selectedExercise.value!!
        val exercise =
            if (selectedExercise.isHaveWeight)
                if (selectedExercise.calculation == Calculation.REPETITIONS)
                    AddedExercise(
                        selectedExercise.name,
                        "${binding.repetitions.text} раз",
                        "${binding.rounds.text} сет.",
                        "${binding.weight.text} кг"
                    )
                else
                    AddedExercise(
                        selectedExercise.name,
                        "${binding.repetitions.text} мин.",
                        "${binding.rounds.text} сет.",
                        "${binding.weight.text} кг"
                    )
            else
                if (selectedExercise.calculation == Calculation.REPETITIONS)
                    AddedExercise(
                        selectedExercise.name,
                        "${binding.repetitions.text} раз",
                        "${binding.rounds.text} сет.",
                        null
                    )
                else
                    AddedExercise(
                        selectedExercise.name,
                        "${binding.repetitions.text} мин.",
                        "${binding.rounds.text} сет.",
                        null
                    )
        val exercises = mutableListOf<AddedExercise>()
        if (!viewModel.addedExercises.value.isNullOrEmpty())
            exercises.addAll(viewModel.addedExercises.value!!)
        exercises.add(exercise)
        viewModel.addedExercises.value = exercises
        viewModel.isAddingNewExercise.value = false
    }

    fun checkFieldsEmpty(): Boolean {
        if ((binding.name.selectedItem as String?).isNullOrEmpty())
            return false
        if (viewModel.selectedExercise.value == null)
            return false
        val exercise = viewModel.selectedExercise.value!!
        if (exercise.isHaveWeight) {
            return (binding.repetitions.text.isNotEmpty()
                    && binding.rounds.text.isNotEmpty()
                    && binding.weight.text.isNotEmpty())
        } else
            return (binding.repetitions.text.isNotEmpty()
                    && binding.rounds.text.isNotEmpty())
    }

    fun addNote() {
        if (viewModel.startTime.isEmpty() || viewModel.endTime.isEmpty()) {
            makeToast(requireContext(), "Не указано время")
            return
        }
        if (viewModel.addedExercises.value.isNullOrEmpty()) {
            makeToast(requireContext(), "Не добавлено ни одного упражнения")
            return
        }
        val ref = Firebase.database.reference.child(NODE_USERS).child(viewModel.user.login)
            .child(NODE_TRAIN_NOTES).child(date!!)
        ref.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var maxId = 0
                    snapshot.children.forEach {
                        val id = it.child("id").getValue(Int::class.java)
                        if (id != null)
                            if (maxId < id) maxId = id
                    }
                    val trainNote = TrainNote(
                        maxId + 1,
                        date!!,
                        viewModel.startTime,
                        viewModel.endTime,
                        viewModel.selectedGroup.value!!,
                        viewModel.addedExercises.value!!,
                        binding.notes.text.toString()
                    )
                    ref.child("${maxId + 1}").setValue(trainNote)
                    viewModel.sendNotificationNewNote(
                        PushNotification(
                            NotificationDate(
                                "Новая тренировка",
                                "Тренер поставил вам новую тренировку\n${
                                    date!!.replace(
                                        ":",
                                        "."
                                    )
                                }\nс ${viewModel.startTime} до ${viewModel.endTime}"
                            ),
                            "/topics/${viewModel.user.login}"
                        )
                    )
                    requireActivity().supportFragmentManager.popBackStack()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(date: String) =
            AddNewNoteFragment().apply {
                arguments = Bundle().apply {
                    putString(DATE, date)
                }
            }
    }
}