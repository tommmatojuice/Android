package com.example.planer.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.planer.database.entity.Product
import com.example.planer.database.repositories.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application)
{
    private val repository = ProductRepository(application.applicationContext, viewModelScope)

    fun productsByList(listId: Int): LiveData<List<Product>> {
        return repository.productsByList(listId)
    }

    fun insert(product: Product) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(product)
    }

    fun update(product: Product) = viewModelScope.launch(Dispatchers.IO){
        repository.update(product)
    }

    fun delete(product: Product) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(product)
    }
}