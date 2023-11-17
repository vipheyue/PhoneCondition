package com.vipheyue.phonecondition.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.vipheyue.phonecondition.database.ScreenUsageTable
import com.vipheyue.phonecondition.ui.MyApp
import com.vipheyue.phonecondition.utils.Config.LAST_SEND_TIME
import com.vipheyue.phonecondition.utils.Config.SCREEN_TYPE_OFF
import com.vipheyue.phonecondition.utils.Config.SCREEN_TYPE_ON
import com.vipheyue.phonecondition.utils.DataStoreUtils
import com.vipheyue.phonecondition.utils.SendMsg
import com.vipheyue.phonecondition.utils.TimeUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class ScreenOnOffReceiver : BroadcastReceiver() {
	private val TAG = "ScreenOnOffReceiver"
	override fun onReceive(context: Context, intent: Intent) {
		// This method is called when the BroadcastReceiver is receiving an Intent broadcast.
		if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			// 处理屏幕点亮的逻辑
			Log.d(TAG, "处理屏幕点亮的逻辑")

			var screenUsageTable = ScreenUsageTable()
			screenUsageTable.timestamp = System.currentTimeMillis()
			screenUsageTable.screen_on_time = TimeUtil.getCurrentTime()
			screenUsageTable.screen_type = SCREEN_TYPE_ON

			GlobalScope.launch {
				//异步执行的协程语句块
				MyApp.appDB.screenDao().insertAll(screenUsageTable)
			}

		}

		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			// 处理屏幕关闭的逻辑
			Log.d(TAG, "处理屏幕关闭的逻辑")

			var offItem = ScreenUsageTable()
			offItem.timestamp = System.currentTimeMillis()
			offItem.screen_off_time = TimeUtil.getCurrentTime()
			offItem.screen_type = SCREEN_TYPE_OFF

			GlobalScope.launch {
				//异步执行的协程语句块
				// 查出上一次亮屏的时间戳 计算使用时长
				var lastScreenOnItem = MyApp.appDB.screenDao().findLastScreenOnItem(SCREEN_TYPE_ON)
				lastScreenOnItem?.let {
					var useMinutes = (System.currentTimeMillis() - it.timestamp) / 1000 / 60
					offItem.usage_during = useMinutes.toInt()
				}

				MyApp.appDB.screenDao().insertAll(offItem)


				// 如果当前时间等于晚上10点 就调用
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


					var lastSend_dayOfMonth = DataStoreUtils.readSyncData(LAST_SEND_TIME, 0)


					// 从时间戳获取日期

						// 获取当前日期
						var now = LocalDate.now()
						val currentDate = now.dayOfMonth


						if (lastSend_dayOfMonth == currentDate) {
							Log.d(TAG, " 今天已经发送过了 不再发送")
						} else {
							Log.d(TAG, "今天没有发送过 开始发送消息")

								SendMsg.sendTodayRecord()
								// 保存最后一次发送的时间戳
								DataStoreUtils.saveData(LAST_SEND_TIME, currentDate)
						}


				} else {
				}


			}


		}
	}
}