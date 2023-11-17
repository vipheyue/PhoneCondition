package com.vipheyue.phonecondition.ui.home


import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vipheyue.phonecondition.ui.MyApp
import com.vipheyue.phonecondition.utils.Config
import com.vipheyue.phonecondition.utils.SendMsg.sendGetRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.time.Instant

class HomeViewModel : ViewModel() {
	private  val TAG = "HomeViewModel"
	private val _text = MutableLiveData<String>().apply {
		value = "This is home Fragment"
	}
	val text: LiveData<String> = _text


	fun getTodayRecord() {

		GlobalScope.launch {

			val result = withContext(Dispatchers.IO) {
				var stringBuffer = StringBuffer()

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {




					val currentTime = Instant.now()
					val sevenDaysAgo = currentTime.minusSeconds(7 * 24 * 60 * 60)
					val timestamp = sevenDaysAgo.epochSecond*1000


//					MyApp.appDB.screenDao().deleteAllData()

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
				val chunkSize = 800

				val chunks = result.chunked(chunkSize)
				for (chunk in chunks) {
					// 使用示例
					val url = "http://117.50.175.133:8090/2G9AxVYJcpxb3efbCcqeP7/何飞杨手机使用/"+ URLEncoder.encode("$chunk", "UTF-8")
					val response = sendGetRequest(url)
					Log.d(TAG, "发送结果:"+response)
				}
				result
			}

			withContext(Dispatchers.Main) {
				_text.value = result

			}


		}


	}




}