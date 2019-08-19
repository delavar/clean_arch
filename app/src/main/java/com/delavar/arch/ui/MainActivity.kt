package com.delavar.arch.ui

import com.delavar.arch.R
import com.delavar.arch.databinding.ActivityMainBinding
import com.delavar.arch.ui.base.BaseActivity


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override val viewModel: MainViewModel by getLazyViewModel()
    override val layoutId: Int=R.layout.activity_main
}
