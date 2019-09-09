package com.owenlejeune.matchinggame.networking

interface IAPIConfig {

    fun baseURL(): String

    fun accessToken(): String?

}