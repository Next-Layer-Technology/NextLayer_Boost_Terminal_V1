package com.sis.clighteningboost.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context, name: String?) {
    val sp: SharedPreferences
    val et: SharedPreferences.Editor

    //shared Preferences NAMES
    //app_local_data
    init {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        et = sp.edit()
    }

    fun saveBooleanValue(key: String?, value: Boolean) {
        et.putBoolean(key, value).apply()
    }

    fun getBooleanValue(key: String?): Boolean {
        return sp.getBoolean(key, false)
    }

    fun saveIntValue(key: String?, value: Int) {
        et.putInt(key, value).apply()
    }

    fun getIntValue(key: String?): Int {
        return sp.getInt(key, 0)
    }

    fun saveStringValue(key: String?, value: String?) {
        et.putString(key, value).apply()
    }

    fun getStringValue(key: String?): String? {
        return sp.getString(key, "")
    }

    fun removeValue(key: String?) {
        et.remove(key).apply()
    }

    fun clearAll() {
        et.clear().apply()
    }

    fun containKey(key: String?): Boolean {
        return if (sp.contains(key)) {
            true
        } else {
            false
        }
    }

}