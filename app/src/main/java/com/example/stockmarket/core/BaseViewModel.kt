package com.example.stockmarket.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(dispatcher: CoroutineContext) : ViewModel() {

    var job = SupervisorJob()
    val scope by lazy { CoroutineScope(dispatcher + job) }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
