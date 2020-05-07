package com.bguneys.app652020.info

import com.squareup.moshi.Json

data class User (
    val id: Int,
    val name: String,
    @Json(name = "email") val e_mail: String
    )
