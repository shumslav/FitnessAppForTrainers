package com.example.fitnessapp.viewModels

import NODE_CLIENTS
import NODE_TRAINERS
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.Client
import com.example.fitnessapp.models.CurrentUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ClientsViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {

    val clients: MutableLiveData<MutableList<Client>> = MutableLiveData()
    val currentUser: CurrentUser = CurrentUser(myApplication)

    init {
        Firebase.database.reference.child(NODE_TRAINERS).child(currentUser.login)
            .child(NODE_CLIENTS).addValueEventListener(
                object: ValueEventListener{
                    val result = mutableListOf<Client>()
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            result.add(it.getValue(Client::class.java)!!)
                        }
                        clients.value = result
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                }
            )
    }
}