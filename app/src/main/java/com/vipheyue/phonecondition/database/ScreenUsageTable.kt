package com.vipheyue.phonecondition.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
data class ScreenUsageTable(


	@PrimaryKey(autoGenerate = true)
	var id: Int = 0,
	@ColumnInfo(name = "timestamp")
	var timestamp: Long = 0,
	@ColumnInfo(name = "screen_on_time")
	var screen_on_time: String = "",

	@ColumnInfo(name = "screen_off_time")
	var screen_off_time: String = "",

	/**
	 * 单次使用时长
	 */
	@ColumnInfo(name = "usage_during")
	var usage_during: Int = 0,


	@ColumnInfo(name = "today_use_counts")
	var today_use_counts: Int = 0,// 今日使用次数)


	@ColumnInfo(name = "screen_type")
	var screen_type: String = "",
) {
	@Ignore
	constructor():this(0,)

}
