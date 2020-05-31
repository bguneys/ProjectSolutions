package com.everydaysolutions.bguneys.everydaysolutions

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.everydaysolutions.bguneys.everydaysolutions.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTest {

    private lateinit var projectDatabase : ProjectDatabase
    private lateinit var folderDao : FolderDao
    private lateinit var planDao : PlanDao

    //Scope and Job variables for Coroutine use
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    @Before
    fun createDatabase() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        projectDatabase = Room.inMemoryDatabaseBuilder(appContext, ProjectDatabase::class.java)
            .allowMainThreadQueries().build()
        folderDao = projectDatabase.folderDao
        planDao = projectDatabase.planDao
    }

    @After
    fun closeDatabase() {
        try {
            projectDatabase.close()
        } catch(e : Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Test for inserting Folder item inside database
     */
    @Test
    fun insertFolder() {
        try {
            //create a folder for testing
            val folder = Folder(
                1,
                "Test Folder Title",
                "Test Note Title",
                "Test Note Text")

            //suspend function insert() should be called from a coroutine
            uiScope.launch {
                folderDao.insert(folder)
            }

            //check if test folder is inserted into the database
            val testFolder = folderDao.getFolderList()
            assertEquals(testFolder.value!!.get(0).folderTitle, "Test Folder Title")

        } catch(e : Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Test for inserting Plan item inside database
     */
    @Test
    fun insertPlan() {
        try {
            //create a plan for testing
            val plan = Plan(
                1,
                "Test Event Title",
                "Test Event Description",
                1000000L)

            //suspend function insert() should be called from a coroutine
            uiScope.launch {
                planDao.insert(plan)
            }

            //check if test plan is inserted into the database
            val testPlan = planDao.getAllPlans()
            assertEquals(testPlan.value!!.get(0).planTitle, "Test Event Title")

        } catch(e : Exception) {
            e.printStackTrace()
        }
    }

}
