package {{ cookiecutter.package_dir }}.utils.extension

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

/**
 * Get file name of the file [Uri]
 * @param uri of the file
 * @return file name
 */
fun Context.getFileName(uri: Uri): String? =
    contentResolver.query(uri, null, null, null, null)
        ?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToNext()
            it.getString(nameIndex)
        }
