package com.flow.moviesearch.presentation.ui.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.flow.moviesearch.R

@BindingAdapter("app:loadUrl")
fun loadUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
        .load(url)
        .error(R.drawable.bg_image_none)
        .placeholder(R.drawable.bg_image_placeholder)
        .override(imageView.layoutParams.width, imageView.layoutParams.height)
        .into(imageView)
}