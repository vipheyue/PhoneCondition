package com.vipheyue.phonecondition.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface ScreenUsageDao {
	@Query("SELECT * FROM screenusagetable")
	suspend fun getAll(): List<ScreenUsageTable>


	@Query("SELECT * FROM screenusagetable WHERE timestamp > :deadlineTime  ")
	fun getRecordByDeadline(deadlineTime: Long): List<ScreenUsageTable>

	@Query("SELECT * FROM screenusagetable   ")
	suspend fun getAllRecord(): List<ScreenUsageTable>

	@Insert
	suspend fun insertAll(users: ScreenUsageTable)

	@Delete
	suspend fun delete(user: ScreenUsageTable)

	@Query("SELECT * FROM screenusagetable WHERE screen_type = :screen_type ORDER BY id DESC LIMIT 1  ")
	suspend fun findLastScreenOnItem(screen_type: String): ScreenUsageTable
}