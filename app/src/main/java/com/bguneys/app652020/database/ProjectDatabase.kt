package com.bguneys.app652020.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Folder::class], version = 1, exportSchema = false)
abstract class ProjectDatabase : RoomDatabase() {

    abstract val folderDao : FolderDao

    companion object {

        @Volatile
        private var databaseInstance : ProjectDatabase? = null

        fun getDatabaseInstance(context : Context) : ProjectDatabase {

            synchronized(this) {
                var instance = databaseInstance

                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        ProjectDatabase::class.java, Constants.DATABASE_NAME)
                        .fallbackToDestructiveMigration().build()

                    databaseInstance = instance
                }

                return instance
            }

        }

    }
}