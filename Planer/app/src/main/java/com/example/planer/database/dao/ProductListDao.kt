package com.example.planer.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.planer.database.entity.ListAndAllProducts
import com.example.planer.database.entity.ProductList

@Dao
interface ProductListDao {
    @Insert
    fun insert(productList: ProductList)

    @Update
    fun update(productList: ProductList)

    @Delete
    fun delete(productList: ProductList)

    @Query("DELETE FROM product_list")
    fun deleteAllLists()

    @Query("SELECT * FROM product_list")
    fun allLists(): LiveData<List<ProductList>>

    @Transaction
    @Query("SELECT * FROM product_list ORDER BY title")
    fun listWithProducts(): LiveData<List<ListAndAllProducts>>
}