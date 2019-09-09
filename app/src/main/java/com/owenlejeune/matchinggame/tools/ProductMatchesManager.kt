package com.owenlejeune.matchinggame.tools

import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.owenlejeune.matchinggame.ui.component.ProductCard

class ProductMatchesManager {

    private val WINNING_MATCHES = 10
    private val NEEDED_FOR_MATCH = 2

    private val visibleCards = ArrayList<ProductCard>(NEEDED_FOR_MATCH)
    private val _numMatches = MutableLiveData<Int>()
    val numMatches: LiveData<Int>
        get() = _numMatches
    private var allMatchesFoundListener: AllMatchesFoundListener? = null

    init {
        _numMatches.postValue(0)
    }

    fun registerCard(owner: LifecycleOwner, card: ProductCard) {
        card.facedown.observe(owner, Observer { isFacedown ->
            if (isFacedown) {
                visibleCards.remove(card)
            } else {
                visibleCards.add(card)
                if (visibleCards.size == NEEDED_FOR_MATCH) checkMatch()
            }
        })
    }

    private fun checkMatch() {
        if (visibleCards[0] == visibleCards[1]) {
            visibleCards.forEach { it.setFlippable(false) }
            _numMatches.postValue(_numMatches.value!! + 1)
            visibleCards.clear()
            if (_numMatches.value!!+1 == WINNING_MATCHES) allMatchesFoundListener?.onAllMatchesFound()
        } else {
            Handler().postDelayed({
                visibleCards.forEach { it.flip() }
                visibleCards.clear()
            }, 500)
        }
    }

    fun reset() {
        _numMatches.postValue(0)
        visibleCards.clear()
    }

    fun setMatchesFoundListener(listener: AllMatchesFoundListener) {
        allMatchesFoundListener = listener
    }

    interface AllMatchesFoundListener {
        fun onAllMatchesFound()
    }

}