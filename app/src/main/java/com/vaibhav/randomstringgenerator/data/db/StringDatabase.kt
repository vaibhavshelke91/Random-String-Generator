package com.vaibhav.randomstringgenerator.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(version = 1, entities = [StringModelEntity::class])
abstract class StringDatabase : RoomDatabase() {

    abstract fun getStringDao(): StringDao


    companion object {
        var instance: StringDatabase? = null

        fun getDatabase(context: Context) :StringDatabase{
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context,
                        StringDatabase::class.java,
                        "string_db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }
}