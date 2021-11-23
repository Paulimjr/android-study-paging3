package com.androidstudy.service.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidstudy.service.local.CharacterDAO
import com.androidstudy.service.local.CharacterRemoteKeyDAO

@Database(entities = [CharacterModel::class, CharacterRemoteKey::class], version = 2, exportSchema = false)
abstract class CharacterDB : RoomDatabase() {

    abstract fun characterDao(): CharacterDAO
    abstract fun characterRemoteKeyDao(): CharacterRemoteKeyDAO

    companion object {

        @Volatile private var instance: CharacterDB? = null

        fun getDatabase(context: Context): CharacterDB =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, CharacterDB::class.java, "android-study-db")
                .fallbackToDestructiveMigration()
                .build()
    }
}