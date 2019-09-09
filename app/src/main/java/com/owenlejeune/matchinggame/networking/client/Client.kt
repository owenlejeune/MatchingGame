package com.owenlejeune.matchinggame.networking

import com.owenlejeune.matchinggame.networking.config.IAPIConfig
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import retrofit2.Retrofit

class Client: KoinComponent {

    private val config: IAPIConfig by inject()
    private val client: Retrofit

    init {
        client = Retrofit.Builder()
            .baseUrl(config.baseURL())
            .addConverterFactory(get())
            .build()
    }

    fun <S> createService(serviceClass: Class<S>): S = client.create(serviceClass)

}