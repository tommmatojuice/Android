package com.example.planer.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "product_table",
        foreignKeys = arrayOf(
                ForeignKey(entity = ProductList::class,
                        parentColumns = arrayOf("product_list_id"),
                        childColumns = arrayOf("list"),
                        onUpdate = ForeignKey.CASCADE,
                        onDelete = ForeignKey.CASCADE
                )
        )
)
class Product(
    var name: String,
    var count: Double,
    var cost: Double,
    var isBought: Boolean,
    var list: Int
):Serializable
{
    @PrimaryKey(autoGenerate = true)
    var product_id = 0
}