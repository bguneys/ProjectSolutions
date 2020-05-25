package com.bguneys.app652020.info

import com.squareup.moshi.Json

data class InfoItem (
    val id: Int,
    val title: String,
    val description: String,
    val url: String
    )
