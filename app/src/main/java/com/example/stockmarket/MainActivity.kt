package com.example.stockmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.stockmarket.data.*
import com.example.stockmarket.ui.stockprice.StockPriceViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    val scope = MainScope()

    private val scarlet by inject<StockService>()
    private val vm by viewModel<StockPriceViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        scope.launch {
//            scarlet.observeWebSocketEvent().consumeEach { event ->
//                println("porcodio " + event)
//                if(event is WebSocket.Event.OnConnectionOpened<*>){
//                    stockList.forEach {
//                        scarlet.subscribe(SubscribeCommand(it))
//                    }
//
//                    while(true){
//                        println("porcodio " + scarlet.observeStock().receive())
//                    }
//                }
//            }
//
//        }

        vm.liveData.observe(this, Observer {
            println("porcodio " + it)
        })

        vm.subscribeAll()


        tv.setOnClickListener {
            vm.unSubscribeAll()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}