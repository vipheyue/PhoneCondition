package com.vipheyue.phonecondition.utils

import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import com.vipheyue.phonecondition.ui.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.time.Instant

object SendMsg {
	private const val TAG = "SendMsg"
	fun sendMsgBootSuccess() {

		GlobalScope.launch {

			val result = withContext(Dispatchers.IO) {
					// 使用示例
					val url = "http://117.50.175.133:8090/2G9AxVYJcpxb3efbCcqeP7/何飞杨手机使用/"+ URLEncoder.encode("开机启动监听成功", "UTF-8")
					val response = sendGetRequest(url)
					response.length
					Log.d(TAG, "发送结果:"+response)

			}

		}


	}
	fun sendTodayRecord() {

		GlobalScope.launch {

			val result = withContext(Dispatchers.IO) {
				var stringBuffer = StringBuffer()

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

					val currentTime = Instant.now()
					val sevenDaysAgo = currentTime.minusSeconds(4 * 24 * 60 * 60)
					val timestamp = sevenDaysAgo.epochSecond*1000



					var allItem = MyApp.appDB.screenDao().getRecordByDeadline(timestamp)
					for (screenUsageTable in allItem) {
						if (screenUsageTable.screen_type.equals(Config.SCREEN_TYPE_ON)) {
							stringBuffer.append("打开屏幕  ")
						}
						if (screenUsageTable.screen_type.equals(Config.SCREEN_TYPE_OFF)) {
							stringBuffer.append("关闭屏幕  ")
						}

						stringBuffer.append(screenUsageTable.screen_on_time)
						stringBuffer.append(screenUsageTable.screen_off_time)
						stringBuffer.append(" 使用 " + screenUsageTable.usage_during + " 分")
						stringBuffer.append(System.lineSeparator())
					}
				}

				var result = stringBuffer.toString()


				// 数据过长
				val chunkSize = 1000

				val chunks = result.chunked(chunkSize)
				for (chunk in chunks) {
					// 使用示例
					val url = "http://117.50.175.133:8090/2G9AxVYJcpxb3efbCcqeP7/何飞杨手机使用/"+ URLEncoder.encode("$chunk", "UTF-8")
					val response = sendGetRequest(url)
					response.length
					Log.d(TAG, "发送结果:"+response)

				}


				result
			}



		}


	}


	fun sendGetRequest(urlString: String): String {
		val url = URL(urlString)
		val connection = url.openConnection() as HttpURLConnection
		connection.requestMethod = "GET"

		val responseCode = connection.responseCode
		if (responseCode == HttpURLConnection.HTTP_OK) {
			val reader = BufferedReader(InputStreamReader(connection.inputStream))
			val response = StringBuilder()
			var line: String?
			while (reader.readLine().also { line = it } != null) {
				response.append(line)
			}
			reader.close()
			return response.toString()
		} else {
			throw Exception("HTTP GET request failed with response code: $responseCode")
		}
	}


}