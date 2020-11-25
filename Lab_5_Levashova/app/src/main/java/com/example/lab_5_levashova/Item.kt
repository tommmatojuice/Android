package com.example.lab_5_levashova

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item_table")
class Item(
    var title: String,
    var description: String,
    var priority: Boolean
):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var id = 0
}