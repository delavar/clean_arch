package com.delavar.arch.di.qualifier

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Custom [MapKey] that accepts a [KClass] that extends [ViewModel] and use it as the key of ViewModels.
 *
 * @see com.delavar.arch.di.builder.ViewModelBuilder
 */
@MustBeDocumented
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)