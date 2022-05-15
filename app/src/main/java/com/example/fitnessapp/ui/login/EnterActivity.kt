package com.example.fitnessapp.ui.login

import NODE_PASSWORD
import NODE_PASSWORDS
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.ActivityEnterBinding
import com.example.fitnessapp.databinding.FragmentScheduleBinding
import com.example.fitnessapp.ui.MainActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import makeToast

class EnterActivity : AppCompatActivity() {

    lateinit var binding: ActivityEnterBinding
    lateinit var user: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = getSharedPreferences("user", MODE_PRIVATE)

        if (user.contains("login")) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter)
        binding.fragment = this
    }

    fun enter() {
        if (checkEmptyFields()) {
            val loginText = binding.login.text.toString()
            val passwordText = binding.password.text.toString()
            Firebase.database.reference.child(NODE_PASSWORDS).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(loginText)) {
                            val password =
                                snapshot.child(loginText)
                                    .child(NODE_PASSWORD)
                                    .getValue(String::class.java)
                            if (passwordText == password) {
                                user.edit().putString("login", loginText).apply()
                                startActivity(Intent(this@EnterActivity, MainActivity::class.java))
                            } else
                                makeToast(this@EnterActivity, "Неправильный пароль")
                        } else
                            makeToast(this@EnterActivity, "Пользователь не найден")
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
        } else
            makeToast(this, "Заполните все поля")
    }

    private fun checkEmptyFields(): Boolean {
        return (binding.login.text.toString() != "" && binding.password.text.toString() != "")
    }

    fun registration() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_fragment, RegistrationFragment()).addToBackStack("registration")
            .commit()
    }
}