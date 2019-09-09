package com.owenlejeune.matchinggame.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.owenlejeune.matchinggame.networking.model.Product
import com.owenlejeune.matchinggame.networking.repository.ProductRepository

class MainViewModel : ViewModel() {

    private val _productLiveData: MutableLiveData<List<Product>> = ProductRepository.getProducts()
    val productLiveData: LiveData<List<Product>>
        get() = _productLiveData

}
