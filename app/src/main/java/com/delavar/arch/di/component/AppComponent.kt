package com.delavar.arch.di.component

import android.app.Application
import com.delavar.arch.app.App
import com.delavar.arch.di.builder.ActivityBuilder
import com.delavar.arch.di.builder.ViewModelBuilder
import com.delavar.arch.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class,
        ViewModelBuilder::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }
}