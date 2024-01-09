#!/bin/bash

initialize_git() {
    echo "=> Initializing git"
    git init
    git add -A
}

load_static_code_analysis() {
    echo "=> Load static code analysis"
    git submodule add https://github.com/SimformSolutionsPvtLtd/static_analysis_android.git settings
    git submodule update --init
}

setup_static_code_analysis() {
    cd settings
    chmod -R 777 setup.sh
    ./setup.sh
    cd ..
}

update_project_permissions() {
    echo "=> Updating project permissions"
    cd ..
    chmod -R 777 "{{ cookiecutter.repo_name }}"
    cd "{{ cookiecutter.repo_name }}"
}

remove_unwanted_files() {
    echo "=> Removing unwanted files"
    # Remove Room DB files
    if [[ "{{ cookiecutter.include_room_db}}" = 'n' ]]; then
        rm -rf "app/src/main/java/{{ cookiecutter.package_dir }}/data/local/"
        rm -rf "app/src/main/java/{{ cookiecutter.package_dir }}/data/local/"
        echo "Removed Room DB files"
    fi
    # Remove Test files
    if [[ "{{ cookiecutter.include_testing}}" = 'n' ]]; then
        rm -rf "app/src/test/"
        echo "Removed Test files"
    fi
}

attempt_to_launch_studio() {
    if [[ -z ${CI+x} ]]; then
        check_for_launch_flag
    else
        echo "Skipping the launching of Android Studio because we're building on CI..."
    fi
}

check_for_launch_flag() {
    if [[ "{{ cookiecutter.launch_studio }}" = true ]]; then
        launch_studio
    else
        echo "Skipping the launching of Android Studio..."
    fi
}

apply_copyright() {
    echo "=> Running spotless to add copyright"
    ./gradlew spotlessApply
}

launch_studio() {
    if [[ "$OSTYPE" == "darwin"* ]]; then
        /Applications/Android\ Studio.app/Contents/MacOS/studio .
        echo "Android Studio should now be running, have fun with your new project!"
    else
        echo "Unsupported operating system: skipping the launching of Android Studio..."
    fi
}

initialize_git
load_static_code_analysis
setup_static_code_analysis
update_project_permissions
remove_unwanted_files
apply_copyright
attempt_to_launch_studio