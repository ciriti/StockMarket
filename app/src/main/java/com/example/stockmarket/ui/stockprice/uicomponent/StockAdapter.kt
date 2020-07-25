package com.example.stockmarket.ui.stockprice.uicomponent

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmarket.ui.stockprice.StockInfoUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class StockAdapter(private val scope: CoroutineScope) : RecyclerView.Adapter<StockAdapter.ViewHolder() {

    private val workerDispatcher: CoroutineContext by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ViewHolder(
        val view: View,
        var item: StockInfoUi,
    ) : RecyclerView.ViewHolder(view) {

    }

}