package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.ProductListDao
import com.example.planer.database.entity.ListAndAllProducts
import com.example.planer.database.entity.ProductList
import kotlinx.coroutines.CoroutineScope

class ProductListRepository(context: Context, scope: CoroutineScope)
{
    private val productListDao: ProductListDao
    val allLists: LiveData<List<ProductList>>

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)
        productListDao = database.productListDao()
        allLists = productListDao.allLists()
    }

    fun listWithProducts(): LiveData<List<ListAndAllProducts>> {
        return productListDao.listWithProducts()
    }

    fun insert(productList: ProductList){
        productListDao.insert(productList)
    }

    fun update(productList: ProductList){
        productListDao.update(productList)
    }

    fun delete(productList: ProductList){
        productListDao.delete(productList)
    }
}