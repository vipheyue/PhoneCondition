package com.vipheyue.phonecondition.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.vipheyue.phonecondition.MainActivity
import com.vipheyue.phonecondition.database.ScreenUsageTable
import com.vipheyue.phonecondition.service.ForegroundService
import com.vipheyue.phonecondition.ui.MyApp
import com.vipheyue.phonecondition.utils.Config
import com.vipheyue.phonecondition.utils.SendMsg
import com.vipheyue.phonecondition.utils.TimeUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BootupReceiver : BroadcastReceiver() {
	private val TAG = "BootupReceiver"
	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			Log.d(TAG, "收到开机启动广播")

			var intent = Intent(context, MainActivity::class.java)
//			intent.setAction("android.intent.action.MAIN");
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

			context.startActivity(intent);

//前台服务
			val frontServiceIntent = Intent(context, ForegroundService::class.java)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				context.startForegroundService(frontServiceIntent)
			} else {
				context.startService(frontServiceIntent)
			}



			Log.d(TAG, "开机插入第一条数据")

			var screenUsageTable = ScreenUsageTable()
			screenUsageTable.timestamp = System.currentTimeMillis()
			screenUsageTable.screen_on_time = TimeUtil.getCurrentTime()
			screenUsageTable.screen_type = Config.SCREEN_TYPE_ON

			GlobalScope.launch {
				//异步执行的协程语句块
				MyApp.appDB.screenDao().insertAll(screenUsageTable)
				SendMsg.sendTodayRecord()

			}

		}

	}


}