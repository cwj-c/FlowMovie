package com.flow.moviesearch.presentation.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingActivity<T: ViewDataBinding> : AppCompatActivity() {

    abstract val layoutResId: Int
    private var _binding: T? = null
    protected val binding: T get() = _binding ?: error("Binding not initialized")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
    }
}