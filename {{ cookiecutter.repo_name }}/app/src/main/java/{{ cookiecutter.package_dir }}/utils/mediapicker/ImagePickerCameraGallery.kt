/*
* Copyright 2021
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package {{ cookiecutter.package_name }}.utils.mediapicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.annotations.NotNull
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*

/**
 * ImagePickerCameraGallery class is used for taking image from either camera or gallery or both
 */
class ImagePickerCameraGallery(
        val activity: Activity?,
        private val packageManager: PackageManager?,
        private val onMediaPickerHandlerInterface: OnMediaPickerHandlerInterface?,
        private val imagePickerOptionsChoiceEnum: ImagePickerOptionsChoiceEnum, lifecycle: Lifecycle?
) {
    private  var currentPhotoPath: String?=null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1888
        private const val REQUEST_GALLERY_CAPTURE = 1889
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 100
    }

    init {
        lifecycle?.addObserver(ImagePickerLifeCycleObserver())
    }

    private var selectedOptions = 0
    @SuppressWarnings("your message")
    data class Builder(
            @NotNull var activity: Activity,
            @NotNull var packageManager: PackageManager
    ) {

        private var onMediaPickerHandlerInterface: OnMediaPickerHandlerInterface? = null
        private var lifecycle: Lifecycle? = null
        private var imagePickerOptionsChoiceEnum: ImagePickerOptionsChoiceEnum? = ImagePickerOptionsChoiceEnum.BOTH_GALLERY_AND_CAMERA

        /**
         *This method is used to register callback for handling media received
         */
        fun setOnPickerMediaHandlerInterface(onPickerMediaHandlerInterface: OnMediaPickerHandlerInterface) =
                apply { this.onMediaPickerHandlerInterface = onPickerMediaHandlerInterface }

        /**
         *This method is used to register choice what options needs to give in dialog
         */
        fun setImagePickerOptionsChoice(imagePickerOptionsChoiceEnum: ImagePickerOptionsChoiceEnum) =
                apply { this.imagePickerOptionsChoiceEnum = imagePickerOptionsChoiceEnum }

        @Suppress("unused")
        fun setLifeCycleObject(lifecycle: Lifecycle) =
                apply { this.lifecycle = lifecycle }
        fun build() =
                imagePickerOptionsChoiceEnum?.let { imagePickerChoiceEnum ->
                    ImagePickerCameraGallery(
                            activity,
                            packageManager,
                            onMediaPickerHandlerInterface,
                            imagePickerChoiceEnum,lifecycle
                    )

                }
    }

    /**
     *This method is used to open dialog picker
     */
    fun openImagePickerDialog() {

        val options: Array<CharSequence> = when {
            ImagePickerOptionsChoiceEnum.ONLY_CAMERA == imagePickerOptionsChoiceEnum -> {
                arrayOf("Take Photo", "Cancel")
            }
            ImagePickerOptionsChoiceEnum.ONLY_GALLERY == imagePickerOptionsChoiceEnum -> {
                arrayOf("Choose from Gallery", "Cancel")

            }
            else -> {
                arrayOf("Take Photo", "Choose from Gallery", "Cancel")
            }
        }

        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Add Photo!")
        builder.setItems(options) { dialog, item ->
            when {

                options[item] == "Take Photo" -> {
                    dialog.dismiss()
                    takeImageFromCamera()
                }
                options[item] == "Choose from Gallery" -> {
                    dialog.dismiss()
                    takeImageFromGallery()
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()


    }

    /**
     *This method is used to take image from gallery
     */
    private fun takeImageFromGallery() {
        selectedOptions = REQUEST_GALLERY_CAPTURE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkAndRequestPermissionsForGallery()
            ) {
                openGalleryIntent()
            }

        } else {
            openGalleryIntent()
        }

    }

    /**
     *This method is used to take image from camera
     */
    private fun takeImageFromCamera() {
        selectedOptions = REQUEST_IMAGE_CAPTURE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkAndRequestPermissionsForCamera()
            ) {
                openCameraIntent()

            }

        } else {
            openCameraIntent()
        }


    }

    @Throws(ConnectException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
                SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    /**
     *This method is used to open camera intent
     */
    private fun openCameraIntent() {
        selectedOptions = REQUEST_IMAGE_CAPTURE
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            if (packageManager != null) {
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                activity?.applicationContext!!,
                                activity.packageName+".fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }
    /**
     *This method is used to open gallery intent
     */
    private fun openGalleryIntent() {
        selectedOptions = REQUEST_GALLERY_CAPTURE
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity?.startActivityForResult(intent, REQUEST_GALLERY_CAPTURE)
    }

    /**
     *This onActivityResult is used to handle media selection
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val myBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            onMediaPickerHandlerInterface?.onMediaReceived(currentPhotoPath, myBitmap)

        } else if (requestCode == REQUEST_GALLERY_CAPTURE && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            @Suppress("DEPRECATION") val filePath = arrayOf(MediaStore.Images.Media.DATA)
            val c: Cursor? =
                    activity?.contentResolver?.query(selectedImage!!, filePath, null, null, null)
            if (c != null) {
                c.moveToFirst()
                val columnIndex: Int = c.getColumnIndex(filePath[0])
                val picturePath: String = c.getString(columnIndex)
                c.close()

                try {
                    val bitmap = BitmapFactory.decodeFile(picturePath)
                    onMediaPickerHandlerInterface?.onMediaReceived(null, bitmap)
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    onMediaPickerHandlerInterface?.onMediaFailed("Failed")
                }
            }
        }
        else
        {
            onMediaPickerHandlerInterface?.onMediaFailed("Cancelled")
        }


    }

    private fun checkAndRequestPermissionsForCamera(): Boolean {
        val permissionCamera = ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
        )
        val writeStoragePermission =
                ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    private fun checkAndRequestPermissionsForGallery(): Boolean {

        val writeStoragePermission =
                ContextCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)

        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                    activity,
                    listPermissionsNeeded.toTypedArray(),
                    REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }


    fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms: MutableMap<String, Int> = HashMap()
                // Initialize the map with both permissions
                perms[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }



                    // process the normal flow
                    //else any one or both the permissions are not granted

                    if (selectedOptions == REQUEST_IMAGE_CAPTURE) {

                        if (perms[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED
                                && perms[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        ) {
                            takeImageFromCamera()

                        }

                    } else if (selectedOptions == REQUEST_GALLERY_CAPTURE) {
                        if (perms[Manifest.permission.READ_EXTERNAL_STORAGE] == PackageManager.PERMISSION_GRANTED
                        ){
                            takeImageFromGallery()

                        }
                    }

                }
            }
        }
    }
    /**
     *This ImagePickerLifeCycleObserver helper class is used observer activity lifecycle
     */
    class ImagePickerLifeCycleObserver : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun imagePickerListener() {

            //when activity will destroyed this method will execute so we can delete cached files

        }

    }



}
