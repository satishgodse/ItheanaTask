package com.itheanatest.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String?) {
    url?.let {
        Glide.with(imageView.context)
                .load(it)
                .into(imageView)
    }

}

@BindingAdapter("text")
fun setText(textView: TextView, value: String) {
    textView.text = value
}

