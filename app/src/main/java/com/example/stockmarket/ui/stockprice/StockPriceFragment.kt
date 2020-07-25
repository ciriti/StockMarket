package com.example.stockmarket.ui.stockprice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockmarket.R
import com.example.stockmarket.ui.stockprice.uicomponent.StockAdapter
import kotlinx.android.synthetic.main.fragment_stock_price.*
import org.koin.android.viewmodel.ext.android.viewModel

class StockPriceFragment : Fragment() {

    private val viewModel by viewModel<StockPriceViewModel>()
    private val adapter by lazy { StockAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stock_list.layoutManager = LinearLayoutManager(context)
        stock_list.adapter = adapter
        viewModel.liveData.observe(viewLifecycleOwner, Observer { handleResponse(it) })
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribeAll()
    }

    override fun onPause() {
        super.onPause()
        viewModel.unSubscribeAll()
    }

    private fun handleResponse(state: BaseState) = when (state) {
        is BaseState.StateError -> {
            /** Error handling not specified */
        }
        is BaseState.StateSuccess -> {
            println("porcodio " + state.uiStockModel)
            adapter.updateStock(state.uiStockModel)
        }
    }
}
