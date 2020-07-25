package com.example.stockmarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.stockmarket.ui.stockprice.StockPriceViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

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