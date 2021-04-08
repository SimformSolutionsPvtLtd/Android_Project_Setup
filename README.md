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

##Tutorial for implementing image picker
https://sway.office.com/qfCj0V7sH4V2UOCm?authoringPlay=true&publish