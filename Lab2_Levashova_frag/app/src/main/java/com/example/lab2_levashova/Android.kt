package com.example.lab2_levashova

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Android(
    val title: String,
    @DrawableRes val imageAndroid: Int,
    val text: String,
    val data: String,
    val url: String
) :Parcelable