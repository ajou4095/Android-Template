package com.ray.template.data.remote.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ray.template.data.remote.local.database.sample.SampleDao
import com.ray.template.data.remote.local.database.sample.SampleEntity

@Database(entities = [SampleEntity::class], version = 1)
abstract class TemplateDatabase : RoomDatabase() {
    abstract fun sampleDao(): SampleDao

    companion object {
        const val DATABASE_NAME = "template"
    }
}
