package com.example.planer.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

class ListAndAllProducts (
        @Embedded val list: ProductList,
        @Relation(
                parentColumn = "product_list_id",
                entityColumn = "list"
        )
        val products: List<Product>
) : Serializable