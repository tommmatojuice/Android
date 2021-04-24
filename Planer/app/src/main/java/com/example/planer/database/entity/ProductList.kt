package com.example.planer.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product_list")
class ProductList(
        var title: String
):Serializable {
    @PrimaryKey(autoGenerate = true)
    var product_list_id = 0
}