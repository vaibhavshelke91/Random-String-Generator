package com.vaibhav.randomstringgenerator.data.provider

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.google.gson.Gson
import com.vaibhav.randomstringgenerator.data.model.RandomString

import com.vaibhav.randomstringgenerator.data.model.StringModel
import com.vaibhav.randomstringgenerator.utils.CONTENT_URI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StringProvider(private val context: Context) {


    suspend fun getByLength(length:Int): RandomString? = withContext(Dispatchers.IO) {
        try {
            val bundle = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, length)
            }

            val cursor: Cursor? = context.contentResolver.query(CONTENT_URI,null,bundle,null)
            cursor?.use {
                while (it.moveToNext()) {
                    val data = it.getStringOrNull(it.getColumnIndexOrThrow("data"))
                    if (!data.isNullOrEmpty()){
                        val gson = Gson()
                        val model = gson.fromJson(data, RandomString::class.java)
                        return@withContext model
                    }

                }
            }
            null
        }catch (e:Exception){
            e.printStackTrace()
            null
        }

    }




}