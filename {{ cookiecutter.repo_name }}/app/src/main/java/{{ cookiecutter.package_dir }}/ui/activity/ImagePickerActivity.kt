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


package {{ cookiecutter.package_name }}.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.viewModels
import androidx.activity.viewModels
import {{ cookiecutter.package_name }}.R
import {{ cookiecutter.package_name }}.base.BaseAppCompatActivity
import {{ cookiecutter.package_name }}.databinding.ActivityImagePickerBinding
import {{ cookiecutter.package_name }}.viewmodel.SampleViewModel
import {{ cookiecutter.package_name }}.utils.mediapicker.ImagePickerCameraGallery
import {{ cookiecutter.package_name }}.utils.mediapicker.ImagePickerOptionsChoiceEnum
import {{ cookiecutter.package_name }}.utils.mediapicker.OnMediaPickerHandlerInterface

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickerActivity : BaseAppCompatActivity<ActivityImagePickerBinding, SampleViewModel>() {
    override val viewModel: SampleViewModel by viewModels()

    override fun getLayoutResId(): Int = R.layout.activity_image_picker

     var imagePickerCameraGallery: ImagePickerCameraGallery?=null


    override fun initialize() {
        super.initialize()
        binding.button1.setOnClickListener {
            imagePickerCameraGallery?.openImagePickerDialog()
        }
        imagePickerCameraGallery = ImagePickerCameraGallery.Builder(this,packageManager).
        setImagePickerOptionsChoice(ImagePickerOptionsChoiceEnum.BOTH_GALLERY_AND_CAMERA).setOnPickerMediaHandlerInterface(
            object : OnMediaPickerHandlerInterface {
                override fun onMediaReceived(filePath: String?, bitmap: Bitmap?) {
                    binding.imageView1.setImageBitmap(bitmap)
                }

                override fun onMediaFailed(message: String?) {
                    TODO("Not yet implemented")
                }

            }
        )
            .build()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePickerCameraGallery?.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imagePickerCameraGallery?.onActivityResult(requestCode,resultCode,data)
    }



    override fun initializeObservers(viewModel: SampleViewModel) {
        super.initializeObservers(viewModel)


    }


}
