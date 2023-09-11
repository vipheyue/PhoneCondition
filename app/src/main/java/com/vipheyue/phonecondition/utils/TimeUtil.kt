package com.vipheyue.phonecondition.utils

import java.text.SimpleDateFormat
import java.util.Calendar
object TimeUtil {

	fun getCurrentTime(): String {
		val calendar = Calendar.getInstance()
		val dateFormat = SimpleDateFormat("MM-dd HH:mm")
		return dateFormat.format(calendar.time)
	}



}