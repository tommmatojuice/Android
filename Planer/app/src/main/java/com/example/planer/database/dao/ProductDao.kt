package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.Product
import com.example.planer.database.entity.Task

@Dao
interface ProductDao {
    @Insert()
    fun insert(product: Product)

    @Update()
    fun update(product: Product)

    @Delete()
    fun delete(product: Product)

    @Query("DELETE FROM product_table")
    fun deleteAllProducts()

    @Query("SELECT * FROM product_table ORDER BY isBought")
    fun allProduct(): LiveData<List<Product>>

    @Query("SELECT * FROM product_table WHERE list=:listID ORDER BY isBought")
    fun productsByList(listID: Int): LiveData<List<Product>>
}