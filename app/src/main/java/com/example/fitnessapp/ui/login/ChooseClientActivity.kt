package com.example.fitnessapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnessapp.R
import com.example.fitnessapp.adapters.ClientsAdapter
import com.example.fitnessapp.databinding.ActivityChooseClientBinding
import com.example.fitnessapp.models.CurrentUser
import com.example.fitnessapp.viewModels.ClientsViewModel
import com.example.fitnessapp.viewModels.ExercisesViewModel

class ChooseClientActivity : AppCompatActivity() {

    lateinit var binding: ActivityChooseClientBinding
    lateinit var viewModel: ClientsViewModel
    lateinit var user: CurrentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_client)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_client)
        viewModel = ViewModelProvider(this)[ClientsViewModel::class.java]
        user = CurrentUser(this)

        binding.exit.setOnClickListener {
            user.logout()
            this.startActivity(Intent(this,EnterActivity::class.java))
        }

        binding.clientsRecycler.adapter = ClientsAdapter(viewModel,this, this)
        binding.clientsRecycler.layoutManager = LinearLayoutManager(this)
    }
}