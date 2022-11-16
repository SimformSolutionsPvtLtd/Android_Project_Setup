package {{ cookiecutter.package_name }}.utils.extension

import android.widget.EditText

/**
 * Move cursor to end of the text
 */
fun EditText.moveCursorToEnd() = setSelection(text.length)

/**
 * Move cursor to start of the text
 */
fun EditText.moveCursorToStart() = setSelection(0)

/**
 * Checks if text is empty
 */
fun EditText.isEmpty(): Boolean {
    return text.toString().isEmpty()
}

/**
 * Returns length of the text
 */
fun EditText.getTextLength(): Int {
    return text.length
}
