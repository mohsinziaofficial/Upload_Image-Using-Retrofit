package com.example.uploadimageusingretrofit.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UploadResponse(
    @Json(name = "error")
    val error: Boolean?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "image")
    val image: String?,
)