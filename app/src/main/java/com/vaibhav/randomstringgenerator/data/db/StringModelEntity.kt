package com.vaibhav.randomstringgenerator.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StringModelEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int = 0,
    val value:String?=null,
    val length:String?=null,
    val time:String?=null
)