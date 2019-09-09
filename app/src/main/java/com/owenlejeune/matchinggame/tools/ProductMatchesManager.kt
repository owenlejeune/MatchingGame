package com.owenlejeune.matchinggame.tools

import android.os.Handler
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.owenlejeune.matchinggame.ui.component.ProductCard

class ProductMatchesManager {

    private val WINNING_MATCHES = 1//0
    private val NEEDED_FOR_MATCH = 2

    private val visibleCards = ArrayList<ProductCard>(NEEDED_FOR_MATCH)
    private var numMatches: Int = 0
    private var allMatchesFoundListener: AllMatchesFoundListener? = null

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
            numMatches++
            visibleCards.clear()
            if (numMatches == WINNING_MATCHES) allMatchesFoundListener?.onAllMatchesFound()
        } else {
            Handler().postDelayed({
                visibleCards.forEach { it.flip() }
                visibleCards.clear()
            }, 500)
        }
    }

    fun setMatchesFoundListener(listener: AllMatchesFoundListener) {
        allMatchesFoundListener = listener
    }

    interface AllMatchesFoundListener {
        fun onAllMatchesFound()
    }

}