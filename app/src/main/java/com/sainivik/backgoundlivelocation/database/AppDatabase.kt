package com.sainivik.backgoundlivelocation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sainivik.backgoundlivelocation.database.dao.LocationDao
import com.sainivik.backgoundlivelocation.model.LocationTable

@Database(
    entities = [LocationTable::class],
    version = 1,
    exportSchema = false
)


abstract class MyAppDatabase : RoomDatabase() {
    abstract fun getLocationMaster(): LocationDao

    companion object {
        private var instance: MyAppDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): MyAppDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, MyAppDatabase::class.java,
                    "my_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }
    }
}


