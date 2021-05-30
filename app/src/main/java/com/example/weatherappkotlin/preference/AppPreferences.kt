package com.example.weatherappkotlin.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object AppPreferences {
    private const val NAME = "whetherapp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
        Log.e("AppPreferences","init")

    }
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var chooseLanguage: String?
        get() = preferences.getString(ConstantPreference.LANGUAGE, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.LANGUAGE, value!!)
        }
    var bookmarkedDetails: String?
        get() = preferences.getString(ConstantPreference.BOOKMARKED, "")
        set(value) = preferences.edit {
            it.putString(ConstantPreference.BOOKMARKED, value!!)
        }
}