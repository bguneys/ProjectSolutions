package com.everydaysolutions.bguneys.everydaysolutions.database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanRepository (
    val context: Context?
    ) {

    lateinit var planDao: PlanDao

    companion object {
        @Volatile
        private var instance : PlanRepository? = null

        fun getInstance(context: Context): PlanRepository? {
            instance ?: synchronized(this) {
                instance ?: PlanRepository(context).also { instance = it }
            }

            return instance
        }
    }

    init {
        val projectDatabase : ProjectDatabase = ProjectDatabase.getDatabaseInstance(context!!)
        planDao = projectDatabase.planDao
    }

    val planList : LiveData<List<Plan>> = planDao.getAllPlans()

    suspend fun insert(plan : Plan) {
        withContext(Dispatchers.IO) {
            planDao.insert(plan)
        }
    }

    suspend fun update(plan : Plan) {
        withContext(Dispatchers.IO) {
            planDao.update(plan)
        }
    }

    suspend fun delete(plan : Plan) {
        planDao.delete(plan)
    }

}

