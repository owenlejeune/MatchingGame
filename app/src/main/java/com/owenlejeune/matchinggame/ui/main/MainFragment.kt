package com.owenlejeune.matchinggame.ui.main

import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nex3z.flowlayout.FlowLayout
import com.owenlejeune.matchinggame.R
import com.owenlejeune.matchinggame.networking.model.Product
import com.owenlejeune.matchinggame.tools.ProductMatchesManager
import com.owenlejeune.matchinggame.ui.component.ProductCard
import org.koin.android.ext.android.inject

class MainFragment : Fragment(), ProductMatchesManager.AllMatchesFoundListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val matchesManager: ProductMatchesManager by inject()

    private lateinit var viewModel: MainViewModel
    private lateinit var cardContainer: FlowLayout
    private lateinit var scoreTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        cardContainer = view.findViewById(R.id.card_container)
        scoreTextView = view.findViewById(R.id.score_text)

        setHasOptionsMenu(true)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.productLiveData.observe(this@MainFragment, Observer { products ->
            fillBoard(products)
        })

        matchesManager.setMatchesFoundListener(this)
        matchesManager.numMatches.observe(this@MainFragment, Observer { score ->
            scoreTextView.text = resources.getString(R.string.score_text, score)
        })
    }

    private fun fillBoard(products: List<Product>) {
        cardContainer.removeAllViews()
        AsyncTask.THREAD_POOL_EXECUTOR.execute {
            val numRows = cardContainer.height / (ProductCard.HEIGHT_PX + cardContainer.rowSpacing).toInt()
            val numCols = cardContainer.width / ProductCard.WIDTH_PX
            val numCards = (numCols * numRows) / 2

            val duplicatedProducts = ArrayList<Product>()

            products.subList(0, numCards).forEach {
                duplicatedProducts.add(it)
                duplicatedProducts.add(it)
            }

            duplicatedProducts.shuffle()
            duplicatedProducts.forEach {
                activity!!.runOnUiThread {
                    val card = ProductCard(requireContext())
                    card.bindProductToView(it)
                    cardContainer.addView(card)
                    matchesManager.registerCard(this@MainFragment, card)
                }
            }

            Thread.sleep(3000)
            activity!!.runOnUiThread {
                cardContainer.children.forEach {
                    if (it is ProductCard) it.flip()
                }
            }
        }
    }

    override fun onAllMatchesFound() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_winner)
            .setMessage(R.string.message_winner)
            .setPositiveButton(android.R.string.yes) { _, _ -> newGame()}
            .setNegativeButton(android.R.string.no, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new_game -> {
                newGame()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun newGame() {
        matchesManager.reset()
        viewModel.productLiveData.value?.let { fillBoard(it) }
    }

}
