package com.example.lab_6_levashova

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ImageResponse(

    @Json(name = "id")
    val id: String,

    @Json(name = "author")
    val author: String,

    @Json(name = "width")
    val width: Int,

    @Json(name = "height")
    val height: Int,

    @Json(name = "url")
    val url: String,

    @Json(name = "download_url")
    val download_url: String

)