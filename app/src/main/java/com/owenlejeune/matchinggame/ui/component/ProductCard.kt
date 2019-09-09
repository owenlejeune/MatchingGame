package com.owenlejeune.matchinggame.ui.component

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.owenlejeune.matchinggame.R
import com.owenlejeune.matchinggame.networking.model.Product

class ProductCard(context: Context, attrs: AttributeSet?) : MaterialCardView(context, attrs) {

    constructor(context: Context): this(context, null)

    companion object {
        const val HEIGHT_PX = 200
        const val WIDTH_PX = 200
        private val imageSize = RequestOptions.overrideOf(WIDTH_PX, HEIGHT_PX)
    }

    private val productImageView: AppCompatImageView

    private val cardBack = Glide.with(this).load(R.drawable.card).apply(imageSize)
    private lateinit var productImageRequest: RequestBuilder<Drawable>

    private var canBeFlipped: Boolean = true
    private val _facedown = MutableLiveData<Boolean>()
    val facedown: LiveData<Boolean>
        get() = _facedown
    lateinit var product: Product

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.product_card, this)
        productImageView = view.findViewById(R.id.product_image)
        elevation = 8f
        radius = 8f
        _facedown.value = true

        setOnClickListener { flip()}
    }

    fun bindProductToView(product: Product) {
        this.product = product
        productImageRequest = Glide.with(this)
            .load(product.image.url)
            .apply(imageSize)

        productImageRequest.into(productImageView)
    }

    fun flip() {
        if (canBeFlipped) {
            val request = if (_facedown.value!!) productImageRequest else cardBack
            request.into(productImageView)
            _facedown.postValue(!_facedown.value!!)
        }
    }

    fun setFlippable(flippable: Boolean) {
        canBeFlipped = flippable
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is ProductCard) return false
        return product.id == other.product.id
    }

    override fun hashCode(): Int {
        var result = imageSize.hashCode()
        return result
    }

}