package com.vaibhav.randomstringgenerator.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface StringDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stringModelEntity: StringModelEntity)

    @Query("SELECT * FROM stringmodelentity")
    fun getAll(): Flow<List<StringModelEntity>>

    @Delete
    suspend fun delete(stringModelEntity: StringModelEntity)

    @Query("DELETE FROM stringmodelentity")
    suspend fun deleteAll()

}