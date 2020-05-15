package com.bguneys.app652020.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Constants.PLAN_TABLE_NAME)
data class Plan (
    @ColumnInfo(name = Constants.COLUMN_PLAN_ID)
    @PrimaryKey(autoGenerate = true)
    var planId : Int = 0,

    @ColumnInfo(name = Constants.COLUMN_PLAN_TITLE)
    var planTitle : String = "",

    @ColumnInfo(name = Constants.COLUMN_PLAN_DESCRIPTION)
    var planDescription : String = "",

    @ColumnInfo(name = Constants.COLUMN_PLAN_START_DATE)
    var planStartDate :Long = System.currentTimeMillis(),

    @ColumnInfo(name = Constants.COLUMN_PLAN_END_DATE)
    var planEndDate :Long = planStartDate
)
