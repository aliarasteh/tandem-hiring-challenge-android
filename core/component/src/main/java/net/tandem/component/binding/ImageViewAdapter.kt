package net.tandem.component.binding

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.load
import coil.transform.RoundedCornersTransformation

@BindingAdapter(value = ["imageUrl", "placeHolder", "cornerRadius"], requireAll = false)
fun loadImage(
    view: AppCompatImageView,
    imageUrl: String?,
    placeHolder: Drawable?,
    cornerRadius: Float?
) {
    view.load(imageUrl, imageLoader = view.context.imageLoader) {
        cornerRadius?.let {
            transformations(RoundedCornersTransformation(cornerRadius))
        }
        placeHolder?.let {
            error(placeHolder)
            placeholder(placeHolder)
        }
    }
}