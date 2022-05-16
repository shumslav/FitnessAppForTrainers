package com.example.fitnessapp.viewModels

import NODE_MESSAGES
import NODE_USERS
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp.models.CurrentClient
import com.example.fitnessapp.models.CurrentUser
import com.example.fitnessapp.models.Message
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatViewModel(private val myApplication: Application) : AndroidViewModel(myApplication) {
    val messages: MutableLiveData<MutableList<Message>> = MutableLiveData()
    val currentClient = CurrentClient(myApplication)
    val currentUser = CurrentUser(myApplication)
    private val refMessages = Firebase.database.reference.child(NODE_USERS).child(currentClient.login)
        .child(NODE_MESSAGES)

    init {
        refMessages.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val result = mutableListOf<Message>()
                    snapshot.children.forEach {
                        result.add(it.getValue(Message::class.java)!!)
                    }
                    messages.value = result
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}