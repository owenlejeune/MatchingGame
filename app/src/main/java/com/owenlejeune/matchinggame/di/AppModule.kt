package com.owenlejeune.matchinggame.di

import com.owenlejeune.matchinggame.networking.client.Client
import com.owenlejeune.matchinggame.networking.config.IAPIConfig
import com.owenlejeune.matchinggame.networking.config.ShopifyAPIConfig
import com.owenlejeune.matchinggame.tools.ProductMatchesManager
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { Client() }
    single<IAPIConfig> { ShopifyAPIConfig() }
    factory<Converter.Factory> { GsonConverterFactory.create() }
    factory { ProductMatchesManager() }
}