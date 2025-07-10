package com.vaibhav.randomstringgenerator.data.repository

import com.vaibhav.randomstringgenerator.data.provider.StringProvider
import javax.inject.Inject

class StringRepository @Inject constructor(
    private val stringProvider: StringProvider
) {


    suspend fun getByLength(len:Int) =  stringProvider.getByLength(len)


}