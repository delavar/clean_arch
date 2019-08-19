package com.delavar.arch.di.builder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.delavar.arch.CustomViewModelFactory
import com.delavar.arch.di.qualifier.ViewModelKey
import com.delavar.arch.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelBuilder {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMianViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: CustomViewModelFactory): ViewModelProvider.Factory
}