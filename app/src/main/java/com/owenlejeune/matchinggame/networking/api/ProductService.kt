package com.owenlejeune.matchinggame.networking.api

import com.owenlejeune.matchinggame.networking.model.ProductResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {

    @GET("admin/products.json")
    fun getProducts(@Query("page") page: Int, @Query("access_token") accessToken: String): Call<ProductResponse>

}