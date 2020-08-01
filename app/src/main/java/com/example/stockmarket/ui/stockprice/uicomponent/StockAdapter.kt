package com.example.stockmarket.ui.stockprice.uicomponent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmarket.R
import com.example.stockmarket.data.stockList
import com.example.stockmarket.ui.stockprice.StockInfoUi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class StockAdapter(stocks: List<String> = stockList) :
    RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    private var onItemClickListener : ((StockInfoUi) -> Unit)? = null

    private val list: List<StockInfoUi> = stocks.map { StockInfoUi(isin = it, price = "-") }
    private val map: Map<String, Int> = list
        .mapIndexed { index, stockInfoUi -> (stockInfoUi.isin to index) }
        .associate { it }

    private val workerDispatcher: CoroutineContext by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ViewHolder(view, onItemClickListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        // TODO  https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.Adapter#onBindViewHolder(VH,%20int,%20java.util.List%3Cjava.lang.Object%3E)
        /**
         * If the payloads list is not empty
         *              the ViewHolder is currently bound to old data and Adapter may run an efficient partial update using the payload info.
         * If the payload is empty
         *              Adapter must run a full bind.
         */
        if (payloads.isNotEmpty()) {
            holder.update(list[position])
        } else
            super.onBindViewHolder(holder, position, payloads)
    }

    fun updateStock(stock: StockInfoUi) {
        map[stock.isin]?.let { index ->
            list[index].price = stock.price
            notifyItemChanged(index, "NOT_EMPTY")
        }
    }

    class ViewHolder(
        val view: View,
        var onItemClickListener : ((StockInfoUi) -> Unit)?
    ) : RecyclerView.ViewHolder(view) {
        fun bind(item: StockInfoUi) {
            (view as? StockItem)?.apply {
                bind(item)
                setOnClickListener { onItemClickListener?.invoke(item) }
            }
        }
        fun update(item: StockInfoUi) = (view as? StockItem)?.updatePrice(item)
    }

    fun onItemClick(listener : ((StockInfoUi) -> Unit)){
        onItemClickListener = listener
    }

}