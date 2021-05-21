package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.ListAndAllProducts
import com.example.planer.database.entity.ProductList
import com.example.planer.database.repositories.ProductListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductListViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = ProductListRepository(application.applicationContext, viewModelScope)

    fun listWithProducts(): LiveData<List<ListAndAllProducts>> {
        return repository.listWithProducts()
    }

    fun insert(productList: ProductList) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(productList)
    }

    fun update(productList: ProductList) = viewModelScope.launch(Dispatchers.IO){
        repository.update(productList)
    }

    fun delete(productList: ProductList) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(productList)
    }
}