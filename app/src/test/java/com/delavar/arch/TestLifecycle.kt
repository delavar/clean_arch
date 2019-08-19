package com.delavar.arch

import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LifecycleOwner


class TestLifecycle private constructor() : LifecycleOwner {
    private val registry = LifecycleRegistry(this)

    val currentState: Lifecycle.State
        @NonNull
        get() = registry.currentState

    fun create(): TestLifecycle {
        return handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun start(): TestLifecycle {
        return handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    fun resume(): TestLifecycle {
        return handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun pause(): TestLifecycle {
        return handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    fun stop(): TestLifecycle {
        return handleLifecycleEvent(Lifecycle.Event.ON_STOP)
    }

    fun destroy(): TestLifecycle {
        return handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }

    private fun handleLifecycleEvent(@NonNull event: Lifecycle.Event): TestLifecycle {
        registry.handleLifecycleEvent(event)
        return this
    }

    @NonNull
    override fun getLifecycle(): Lifecycle {
        return registry
    }

    companion object {

        fun initialized(): TestLifecycle {
            return TestLifecycle()
        }
    }
}