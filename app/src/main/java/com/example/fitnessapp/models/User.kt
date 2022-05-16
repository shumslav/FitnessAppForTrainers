package com.example.fitnessapp.models

data class User(
    val login:String = "",
    val password:String = "",
    val name:String = "",
    val clients: MutableList<String> = mutableListOf()
)