package {{ cookiecutter.package_dir }}.utils.databinding

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * Set view's visibility gone.
 * @param isGone true to make view gone
 */
@BindingAdapter("showUnlessGone")
fun showUnlessGone(view: View, isGone: Boolean) {
    view.isGone = isGone
}

/**
 * Set view's visibility invisible.
 * @param isInvisible true to make view invisible
 */
@BindingAdapter("showUnlessInvisible")
fun showUnlessInvisible(view: View, isInvisible: Boolean) {
    view.isInvisible = isInvisible
}

/**
 * Load image into [imageView] from the [imageUri].
 */
@BindingAdapter(value = ["imageUri", "placeholder"], requireAll = false)
fun imageUri(imageView: ImageView, imageUri: Uri?, placeholder: Drawable?) {
    Glide.with(imageView)
        .load(imageUri)
        .placeholder(placeholder)
        .into(imageView)
}

/**
 * Load image into [imageView] from the [imageUrl].
 */
@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
fun imageUrl(imageView: ImageView, imageUrl: String?, placeholder: Drawable?) {
    imageUri(imageView, imageUrl?.toUri(), placeholder)
}

