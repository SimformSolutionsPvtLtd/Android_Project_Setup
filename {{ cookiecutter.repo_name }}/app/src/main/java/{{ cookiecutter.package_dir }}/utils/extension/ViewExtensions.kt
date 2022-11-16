package {{ cookiecutter.package_name }}.utils.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

/**
 * Helper function to animate fade out the view
 *
 * @param animateDuration duration for the animation
 */
fun View.fadeOut(animateDuration: Long = 2) {
    animate()
        .alpha(0f)
        .setDuration(animateDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                isVisible = false
            }
        })
        .start()
}

/**
 * Helper function to animate fade in of the view
 *
 * @param animateDuration duration for the animation
 * @param alpha
 */
fun View.fadeIn(animateDuration: Long = 2, alpha: Float = 1f) {
    animate()
        .alpha(alpha)
        .setDuration(animateDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                isVisible = true
            }
        })
        .start()
}

/**
 * Load image from url
 *
 * @param imgUrl url of image
 * @param imageView in which image will load
 * @param error Drawable image in case of error
 * @param placeHolder for image
 */
fun ImageView.loadImage(imgUrl: String, errorDrawable: Drawable? = null, placeHolderDrawable: Drawable? = null) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(placeHolderDrawable)
        .error(errorDrawable)
        .into(this)
}

/**
 * Load image from url
 *
 * @param imgUrl url of image
 * @param imageView in which image will load
 * @param error image resource in case of error
 * @param placeHolder for image
 */
fun ImageView.loadImage(imgUrl: String, error: Int? = null, @IdRes placeHolder: Int = 0) {
    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(placeHolder)
        .error(error)
        .into(this)
}

/**
 * Hide the soft keyboard
 *
 * @param view  The view that is in focus for the keyboard
 */
fun View.hideSoftKeyboard() {
    val imm = this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

/**
 * Shows the Snackbar inside an Activity or Fragment
 *
 * @param messageRes Text to be shown inside the Snackbar
 * @param length Duration of the Snackbar
 * @param action Action of the Snackbar
 */
fun View.showSnackBar(message: String, action: (Snackbar.() -> Unit)? = null, duration: Int) {
    val snackbar = Snackbar.make(this, message, duration)
    action?.let { snackbar.it() }
    snackbar.show()
}
