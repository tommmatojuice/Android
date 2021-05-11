package com.example.planer.database.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.planer.database.MyDataBase
import com.example.planer.database.dao.GroupTaskDao
import com.example.planer.database.dao.ProductDao
import com.example.planer.database.entity.GroupAndAllTasks
import com.example.planer.database.entity.GroupTask
import com.example.planer.database.entity.Product
import kotlinx.coroutines.CoroutineScope

class ProductRepository(context: Context, scope: CoroutineScope)
{
    private val productDao: ProductDao
    val allProducts: LiveData<List<Product>>

    init {
        val database: MyDataBase = MyDataBase.getDatabase(context, scope)!!
        productDao = database.productDao()
        allProducts = productDao.allProduct()
    }

    fun productsByList(listId: Int): LiveData<List<Product>>{
        return productDao.productsByList(listId)
    }

    fun insert(product: Product){
        productDao.insert(product)
    }

    fun update(product: Product){
        productDao.update(product)
    }

    fun delete(product: Product){
        productDao.delete(product)
    }
}