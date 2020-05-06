package com.bguneys.app652020.conversion

import com.squareup.moshi.Json

data class User (
    val id: Int,
    val name: String,
    val username: String,
    @Json(name = "email") val e_mail: String)
