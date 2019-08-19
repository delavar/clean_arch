package com.delavar.arch.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel ()
    : ViewModel(), LifecycleObserver {

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }


    fun onTokenExpired() {
        // todo : what should i do if token expired ?!
    }

    /**
     * We can use lifeCycle in inherited classes if we need
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreateView() {}

    /**
     * We can use lifeCycle in inherited classes if we need
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {}
}