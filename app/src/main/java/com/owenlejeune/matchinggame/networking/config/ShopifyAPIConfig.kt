package com.owenlejeune.matchinggame.networking

class ShopifyAPIConfig: IAPIConfig {

    override fun baseURL(): String = "https://shopicruit.myshopify.com"

    override fun accessToken(): String? = "c32313df0d0ef512ca64d5b336a0d7c6"

}