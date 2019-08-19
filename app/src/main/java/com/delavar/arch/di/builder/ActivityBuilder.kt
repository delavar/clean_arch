package com.delavar.arch.di.builder

import com.delavar.arch.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector()
    internal abstract fun bindMainActivity(): MainActivity
}