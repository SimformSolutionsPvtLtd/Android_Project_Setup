package {{ cookiecutter.package_name }}.utils.extension

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

/**
 * Set a drawable to the left and right of a TextView.
 *
 * @param drawable
 */
fun TextView.setDrawableLeftRight(drawable: Int, isRightDrawable: Boolean) {
    this.setCompoundDrawablesWithIntrinsicBounds(if (isRightDrawable) 0 else drawable, 0, if (isRightDrawable) drawable else 0, 0)
}

/**
 * Set a drawable to the top and bottom of a TextView.
 *
 * @param drawable
 */
fun TextView.setDrawableTopBottom(drawable: Int, isBottomDrawable: Boolean) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, if (isBottomDrawable) 0 else drawable, 0, if (isBottomDrawable) drawable else 0)
}

/**
 * Show the soft keyboard
 *
 * @receiver Textview that is in focus for the keyboard
 */
fun TextView.showSoftKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}
