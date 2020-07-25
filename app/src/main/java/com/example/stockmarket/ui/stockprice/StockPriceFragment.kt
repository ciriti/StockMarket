package com.example.stockmarket.ui.stockprice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stockmarket.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class StockPriceFragment : Fragment() {

    private val job = SupervisorJob()
    private val scope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main + job) }
    private val viewModel by viewModel<StockPriceViewModel>()
    private val adapter by lazy {
        CurrenciesAdapter(scope).apply {
            onChangePositionListener = ::scrollToPosition
            onChangeAmountListener = ::changeAmountListener
        }
    }

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
        currencies_list.layoutManager = LinearLayoutManager(context)
        currencies_list.adapter = adapter
        viewModel.liveData.observe(viewLifecycleOwner, Observer { handleResponse(it) })
        viewModel.loadRates()
    }

    private fun scrollToPosition(rate: CurrencyViewBean, pos: Int) {
        currencies_list.scrollToPosition(pos)
        viewModel.loadRates(rate.currency.currencyCode, rate.amountString.toDouble())
    }

    private fun changeAmountListener(item: CurrencyViewBean, amount: String) {
        viewModel.loadRates(date = item.currency.currencyCode, amount = amount.formatToDouble())
    }

    private fun handleResponse(state: BaseState) = when (state) {
        is BaseState.StateError -> { /** Error handling not specified */ }
        is BaseState.StateSuccess -> { adapter.updateData(state.ratesMap) }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
