package com.example.fitnessapp.viewModels

import NODE_GROUP_MUSCLES
import NODE_USERS
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.CurrentClient
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GroupMusclesViewModel(private val myApplication: Application) :
    AndroidViewModel(myApplication) {
    val groupMuscles: MutableLiveData<MutableList<String>> = MutableLiveData()
    val client: CurrentClient = CurrentClient(myApplication)
    var isAddingNewGroup: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        groupMuscles.value = mutableListOf()
        Firebase.database.reference.child(NODE_USERS).child(client.login).child(NODE_GROUP_MUSCLES)
            .addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val result = mutableListOf<String>()
                        snapshot.children.forEach {
                            if (!it.getValue(String::class.java).isNullOrEmpty())
                                result.add(it.getValue(String::class.java)!!)
                        }
                        groupMuscles.value = result
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                }
            )
    }
}