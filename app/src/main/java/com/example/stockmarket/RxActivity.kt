package com.example.stockmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class RxActivity : AppCompatActivity() {

    val ds = mutableListOf<Disposable>()
    fun Disposable.addTo() = ds.add(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx)

        val fruit = Observable.create<String> { observer ->
            "create observable".logThreadName()
            observer.onNext("apple")
            Thread.sleep(800)
            observer.onNext("pineapple")
            Thread.sleep(800)
            observer.onNext("strawberry")
        }

        fruit
            .map { it.logThreadName("first") }
            .subscribeOn(Schedulers.io())
            .map { it.logThreadName("second") }
            .map { it.logThreadName("third") }
            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { it.printThreadName("received") },
//                { th -> println(th.message) }
//            )
//            .addTo()

        val slowFlowable = Flowable.interval(1, TimeUnit.SECONDS)
        val fastFlowable = Flowable.interval(1, TimeUnit.MILLISECONDS)
            .onBackpressureLatest()
        val disposable =
            Flowables.zip(slowFlowable, fastFlowable) { first, second ->
                first to second
            }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe { (first, second) ->
                    logThreadName("Got $first and $second")
                }
//        disposable.dispose()

    }

    override fun onDestroy() {
        super.onDestroy()
        ds.forEach { it.dispose() }
    }
}

fun<T> T.logThreadName(text : String? = null) = apply {
    Log.i(RxActivity::class.simpleName, "[$this] on Thread: ${Thread.currentThread().name} ${text?.let { "- position: $text" }?:""}")
}
fun<T> T.printThreadName(text : String? = null) = apply {
    println("[Thread: ${Thread.currentThread().name}] $text")
}
fun<T> T.printThreadNameThis(text : String? = null) = apply {
    println("[Thread: ${Thread.currentThread().name}] [$this] $text")
}