package com.vaibhav.randomstringgenerator.data.repository

import com.vaibhav.randomstringgenerator.data.db.StringDatabase
import com.vaibhav.randomstringgenerator.data.db.StringModelEntity
import javax.inject.Inject

class StringDatabaseRepository @Inject constructor(
    private val stringDatabase: StringDatabase
) {

    suspend fun deleteAll() =stringDatabase.getStringDao().deleteAll()

    suspend fun insert(stringModelEntity: StringModelEntity) = stringDatabase.getStringDao().insert(stringModelEntity)

    fun getAll() = stringDatabase.getStringDao().getAll()

    suspend fun delete(stringModelEntity: StringModelEntity) = stringDatabase.getStringDao().delete(stringModelEntity)
}