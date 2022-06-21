package net.flow.jetpackmvvm.ext.download

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Process
import android.util.Log
import com.tencent.mmkv.MMKV
import net.flow.jetpackmvvm.base.appContext
import net.flow.jetpackmvvm.ext.util.loge

/**
 */

    val mmkv = MMKV.defaultMMKV()

    fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key,value)
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mmkv.decodeBool(key,defValue)
    }

    fun putString(key: String, value: String) {
        mmkv.encode(key,value)
    }

    fun getString(key: String, defValue: String): String? {
        return mmkv.decodeString(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        mmkv.encode(key,value)
    }

    fun getInt(key: String, defValue: Int): Int {
        return mmkv.decodeInt(key, defValue)
    }

    fun putLong(key: String, value: Long):Boolean {
        return mmkv.encode(key,value)
    }

    fun getLong(key: String, defValue: Long): Long {
        return  mmkv.decodeLong(key,defValue)
    }

    fun removeKey(key: String) {
        "removeKey:$key".loge()
        mmkv.removeValueForKey(key)
    }

    fun clear() {
        mmkv.clearAll()
    }
