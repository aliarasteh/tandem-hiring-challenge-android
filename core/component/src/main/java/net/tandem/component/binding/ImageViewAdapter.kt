package net.tandem.component.binding

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.load

@BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
fun loadImage(view: AppCompatImageView, imageUrl: String?, placeHolder: Drawable?) {
    view.load(imageUrl, imageLoader = view.context.imageLoader) {
        error(placeHolder)
        placeholder(placeHolder)
    }
}