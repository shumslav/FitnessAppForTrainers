package com.example.fitnessapp.models

import android.content.Context
import android.content.Context.MODE_PRIVATE

class CurrentClient(context: Context) {
    companion object {
        private const val CLIENT = "client"
        private const val LOGIN = "login"
        private const val NAME = "name"
    }

    private val user = context.getSharedPreferences(CLIENT, MODE_PRIVATE)

    var login: String
        get() {
            return if (user.contains(LOGIN))
                user.getString(LOGIN, "")!!
            else ""
        }
        set(value) {user.edit().putString(LOGIN,value).apply()}

    var name: String
        get() {
            return if (user.contains(NAME))
                user.getString(NAME, "")!!
            else ""
        }
        set(value) {user.edit().putString(NAME,value).apply()}


    fun isEnterProfile():Boolean{
        return user.contains(LOGIN)
    }

    fun logout(){
        user.edit().remove(LOGIN).remove(NAME).apply()
    }
}