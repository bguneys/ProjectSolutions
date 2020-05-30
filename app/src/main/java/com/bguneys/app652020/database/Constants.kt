package com.everydaysolutions.bguneys.everydaysolutions.database

object Constants {

    //Constants for table and database
    const val NOTE_TABLE_NAME = "notes_table"
    const val PLAN_TABLE_NAME = "plans_table"
    const val DATABASE_NAME = "project_database"

    //Constants for notes_table
    const val COLUMN_RECORD_ID = "record_id"
    const val COLUMN_FOLDER_TITLE = "folder_title"
    const val COLUMN_NOTE_TITLE = "note_title"
    const val COLUMN_NOTE_TEXT = "note_text"

    //Constants for plans_table
    const val COLUMN_PLAN_ID = "plan_id"
    const val COLUMN_PLAN_TITLE = "plan_title"
    const val COLUMN_PLAN_DESCRIPTION = "plan_description"
    const val COLUMN_PLAN_START_DATE = "plan_start_date"
    const val COLUMN_PLAN_END_DATE = "plan_end_date"
}
