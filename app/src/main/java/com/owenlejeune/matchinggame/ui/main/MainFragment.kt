package com.owenlejeune.matchinggame.ui.main

import android.os.AsyncTask
import android.os.Bundle
import android.view.*
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
    private lateinit var mainContainer: FlowLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        mainContainer = view.findViewById(R.id.main_container)

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
    }

    private fun fillBoard(products: List<Product>) {
        mainContainer.removeAllViews()
        AsyncTask.THREAD_POOL_EXECUTOR.execute {
            val numRows = mainContainer.height / (ProductCard.HEIGHT_PX + mainContainer.rowSpacing).toInt()
            val numCols = mainContainer.width / ProductCard.WIDTH_PX
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
                    mainContainer.addView(card)
                    matchesManager.registerCard(this@MainFragment, card)
                }
            }

            Thread.sleep(3000)
            activity!!.runOnUiThread {
                mainContainer.children.forEach {
                    if (it is ProductCard) it.flip()
                }
            }
        }
    }

    override fun onAllMatchesFound() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Winner!")
            .setMessage("You Win!")
            .setPositiveButton("Ok", null)
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
        viewModel.productLiveData.value?.let { fillBoard(it) }
    }

}
