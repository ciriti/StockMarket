package com.example.stockmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stockmarket.data.*
import com.example.stockmarket.utils.stocksList
import com.tinder.scarlet.WebSocket
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.consumeEach
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    val scope = MainScope()

    private val scarlet by inject<StockService>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scope.launch {
            scarlet.observeWebSocketEvent().consumeEach { event ->
                println("porcodio " + event)
                if(event is WebSocket.Event.OnConnectionOpened<*>){
                    stocksList.forEach {
                        scarlet.subscribe(SubscribeCommand(it))
                    }

                    while(true){
                        println("porcodio " + scarlet.observeStock().receive())
                    }
                }
            }

        }



        tv.setOnClickListener {
            stocksList.forEach {
                scarlet.unSubscribe(UnSubscribeCommand(it))
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}