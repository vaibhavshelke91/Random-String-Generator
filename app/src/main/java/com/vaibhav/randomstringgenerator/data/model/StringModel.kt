package com.vaibhav.randomstringgenerator.data.model

import kotlinx.serialization.Serializable



@Serializable
data class RandomString(
    val randomText: StringModel?
)

@Serializable
data class StringModel(
    val value: String,
    val length: String,
    val created: String
)