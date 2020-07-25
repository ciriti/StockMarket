package com.example.stockmarket.ui.stockprice.uicomponent

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.stockmarket.R
import com.example.stockmarket.ui.stockprice.StockInfoUi
import kotlinx.android.synthetic.main.item_list.view.*

class StockItem : ConstraintLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}

fun StockItem.bind(item: StockInfoUi) {
    isin.text = item.isin
    price.text = context.getString(R.string.stock_price, item.price, "€")
}

fun StockItem.updatePrice(item: StockInfoUi) {
    price.text = context.getString(R.string.stock_price, item.price, "€")
}