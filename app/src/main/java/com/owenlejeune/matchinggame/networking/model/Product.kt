package com.owenlejeune.matchinggame.networking.model

import com.google.gson.annotations.SerializedName

data class Product(@SerializedName("id") val id: Long,
                   @SerializedName("title") val title: String,
                   @SerializedName("image") val image: Image) {

    inner class Image(@SerializedName("id") val id: Long,
                      @SerializedName("src") val url: String)

}