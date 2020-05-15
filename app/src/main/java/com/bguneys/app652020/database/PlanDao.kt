package com.bguneys.app652020.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlanDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(plan : Plan)

    @Update
    suspend fun update(plan : Plan)

    @Delete
    suspend fun delete(plan : Plan)

    @Query("SELECT * FROM plans_table ORDER BY plan_end_date DESC ")
    fun getAllPlans() : LiveData<List<Plan>>
}