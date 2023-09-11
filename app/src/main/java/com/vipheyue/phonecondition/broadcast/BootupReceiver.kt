package com.vipheyue.phonecondition.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.vipheyue.phonecondition.MainActivity
import com.vipheyue.phonecondition.service.ForegroundService

class BootupReceiver : BroadcastReceiver() {
	private val TAG = "BootupReceiver"
	override fun onReceive(context: Context, intent: Intent) {
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
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

		}

	}


}