package com.owenlejeune.matchinggame.networking.client

import org.koin.core.KoinComponent

interface IClient {

    fun <S> createService(serviceClass: Class<S>): S

    fun accessToken(): String

}