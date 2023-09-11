package com.vipheyue.phonecondition.ui

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.vipheyue.phonecondition.database.AppDatabase
import com.vipheyue.phonecondition.utils.DataStoreUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyApp : Application() {
	override fun onCreate() {
		super.onCreate()
		MyApp.applicationContext = this;
		DataStoreUtils.init(this)


		GlobalScope.launch {
			//异步执行的协程语句块
			appDB = Room.databaseBuilder(
				MyApp.applicationContext,
				AppDatabase::class.java, "database-screen"
			).build()
		}


	}

	companion object {
		lateinit var applicationContext: Context
		lateinit var appDB: AppDatabase
	}

}