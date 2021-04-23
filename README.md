# Android Project Template

## Prerequisites

### MacOS

* MacOS 10.14 or higher to use Homebrew
* XCode command line tools (Required for Homebrew)
    * ```xcode-select --install```
* Homebrew
    * ```/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"```
* Python3
     * ```brew install python3```
* Cookie Cutter
    * ```brew install cookiecutter```

### Linux

* Python3
    * ```sudo apt-get update && sudo apt-get install python3.6```
* Cookie Cutter
    * ```sudo apt-get install cookiecutter```

### Windows

* Python3
    * [Download](https://www.python.org/downloads/)
    * ADD PYTHON TO ENVIORNMENT PATH
* Cookie Cutter
    * ```pip install --user cookiecutter```


## Usage

* Open terminal at your desired location
* Run ```cookiecutter https://github.com/SimformSolutionsPvtLtd/Android_Project_Setup``` to create template
    * If it asks for "You have .../.cookiecutters/Android_Project_Setup downloaded before. Is it okay to delete and re-download it?" press "yes"

## Required inputs

Input name | Description | Default
--- | --- | --- |
app_name | Application name | App Name
repo_name | Root Folder name | Repository Name
package_name | App package name | com.simformsolutions.app
appcenter_key | AppCenter key | APPCENTER_KEY
package_dir | Package directory generated based on package name | com/simformsolutions/app
copyright_year_name | Copyright year and name based on provided app_name | 2021 App Name
include_testing | Include testing in project or not | 1 (n)
include_room_db | Include Room database or not | 1 (n)
launch_studio | Open Android Studio after project is created. <b>Works only for MacOS</b> | 1 (false)

## After Project Created

* Import project in Android Studio (If not launching when launch_studio asked)
* Sync project with Gradle files
* Rebuild



## Android Picker Feature Implementation Instruction

Step 1 : Need to declare Image Picker Helper class at appropriate level

       var imagePickerCameraGallery: ImagePickerCameraGallery?=null


Step 2 : Initialise above object with customize options

    imagePickerCameraGallery = ImagePickerCameraGallery.Builder(this,packageManager).


Note: Pass constructor parameters are as Activity and Package Manager mandatory.

        setImagePickerOptionsChoice(ImagePickerOptionsChoiceEnum.BOTH_GALLERY_AND_CAMERA).setOnPickerMediaHandlerInterface(

            object : OnMediaPickerHandlerInterface {

                override fun onMediaReceived(filePath: String?, bitmap: Bitmap?) {

                    binding.imageView1.setImageBitmap(bitmap)

                }

                override fun onMediaFailed(message: String?) {
                }

            }
        )
            .build()


(2.1) To get File Path or Bitmap and required to get error message register callback

Method Name                             Description                                                                                                      Usage


(1) SetOnPickerMediaHandlerInterface()     To use for register callback for handling image picker success or failed                                       Optional

(2) SetImagePickerOptionsChoice()          To use if you want show image taking options (1) From gallery only (2) From Camera only (3) From both          Optional
                                       Default: From both options will show

                                           Values: Enum Type

                                           ONLY_GALLERY,
                                           ONLY_CAMERA,
                                           BOTH_GALLERY_AND_CAMERA

(3) SetLifeCycleObject()                   To delete unnecessary cached files which used while taking from camera/galley to write code in below method    Optional


Step 3 : Permission will handle by in built helper class by just putting below code



override fun onRequestPermissionsResult(

        requestCode: Int,

        permissions: Array<String>,

        grantResults: IntArray

    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        imagePickerCameraGallery?.onRequestPermissionsResult(requestCode,permissions,grantResults)

    }


Step 4 :  To handle callback of success and failed of image picker please use below code in on Activity



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        imagePickerCameraGallery?.onActivityResult(requestCode,resultCode,data)

    }