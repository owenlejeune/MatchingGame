package com.owenlejeune.matchinggame.networking.repository

import androidx.lifecycle.MutableLiveData
import com.owenlejeune.matchinggame.networking.api.ProductService
import com.owenlejeune.matchinggame.networking.client.Client
import com.owenlejeune.matchinggame.networking.model.Product
import com.owenlejeune.matchinggame.networking.model.ProductResponse
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository: KoinComponent {

    private const val PAGE = 1

    private val client: Client by inject()
    private val productsApi: ProductService

    init {
        productsApi = client.createService(ProductService::class.java)
    }

    fun getProducts(): MutableLiveData<List<Product>> {
        val productData = MutableLiveData<List<Product>>()
        productsApi.getProducts(PAGE, client.accessToken()).enqueue(object : Callback<ProductResponse> {
            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                productData.value = ArrayList()
            }

            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { productData.value = it.products }
                }
            }
        })
        return productData
    }

}