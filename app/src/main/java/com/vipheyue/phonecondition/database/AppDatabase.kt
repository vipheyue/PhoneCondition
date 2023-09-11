package com.vipheyue.phonecondition.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ScreenUsageTable::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
	abstract fun screenDao(): ScreenUsageDao
}


