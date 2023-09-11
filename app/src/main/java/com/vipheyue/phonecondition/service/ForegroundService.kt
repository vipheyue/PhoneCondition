package com.vipheyue.phonecondition.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.vipheyue.phonecondition.MainActivity
import com.vipheyue.phonecondition.R
import com.vipheyue.phonecondition.broadcast.ScreenOnOffReceiver


class ForegroundService : Service() {
	private  val TAG = "ForegroundService"

	private var notificationManager: NotificationManager? = null
	val FRONT_NOTIFY_ID = 0x1010

	 val FRONT_CHANNEL_ID = "com.vipheyue.phonecondition"
	 val FRONT_CHANNEL_NAME = "heyue phone screen Statistics"

	override fun onBind(intent: Intent): IBinder? {
		Log.d(TAG, "ForegroundService  onBind() called with: intent = $intent")
		return null;
	}


	override fun onCreate() {
		Log.d(TAG, "ForegroundService  onCreate() called with: intent = ")
		super.onCreate()

		notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		startForeground(FRONT_NOTIFY_ID, createForegroundNotification())
		registerScreenReceiver(baseContext)
	}

	/**
	 * 熄屏注册监听
	 */
	lateinit var mScreenReceiver: ScreenOnOffReceiver
	private fun registerScreenReceiver(context: Context) {
		Log.d(TAG, "注册屏幕监听: ")
		val filter = IntentFilter()
		filter.addAction(Intent.ACTION_SCREEN_ON)
		filter.addAction(Intent.ACTION_SCREEN_OFF)
		mScreenReceiver = ScreenOnOffReceiver()
		context.registerReceiver(mScreenReceiver, filter)
	}

	private fun createForegroundNotification(): Notification {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val importance = NotificationManager.IMPORTANCE_HIGH
			val notificationChannel = NotificationChannel(FRONT_CHANNEL_ID, FRONT_CHANNEL_NAME, importance)
			notificationChannel.description = "Frpc Foreground Service"
			notificationChannel.enableLights(true)
			notificationChannel.lightColor = Color.GREEN
			notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
			notificationChannel.enableVibration(true)
			if (notificationManager != null) {
				notificationManager!!.createNotificationChannel(notificationChannel)
			}
		}
		val builder = NotificationCompat.Builder(this, FRONT_CHANNEL_ID)
		builder.setSmallIcon(R.drawable.ic_launcher_apple)
		builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher_apple))
		// TODO: 部分机型标题会重复待排除
		// if (DeviceUtils.getDeviceBrand().contains("Xiaomi")) {
		builder.setContentTitle(getString(R.string.app_name))
		//}
		builder.setContentText("何飞杨请不要关闭这个APP")
		builder.setWhen(System.currentTimeMillis())
		val activityIntent = Intent(this, MainActivity::class.java)
		val flags = if (Build.VERSION.SDK_INT >= 30) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
		val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, flags)
		builder.setContentIntent(pendingIntent)
		return builder.build()
	}

}