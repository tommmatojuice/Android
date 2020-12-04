package com.example.lab_6_levashova

import retrofit2.Call
import retrofit2.http.GET

interface TheImageAPIService{
    @GET("list")
    fun getImages():Call<List<ImageResponse>>
}