#!/bin/bash

initialize_git() {
    git init
    git add -A
}

update_project_permissions() {
    cd ..
    chmod -R 777 "{{ cookiecutter.repo_name }}"
    cd "{{ cookiecutter.repo_name }}"
}

setup_static_code_analysis() {
    cd settings
    chmod -R 777 setup.sh
    ./setup.sh
    cd ..
}

remove_unwanted_files() {
    # Remove Room DB files
    if [[ "{{ cookiecutter.include_room_db}}" = 'n' ]]; then
        rm -d -R "app/src/main/java/{{ cookiecutter.package_dir }}/data/local/db/converter"
        rm -d -R "app/src/main/java/{{ cookiecutter.package_dir }}/data/local/db/dao"
        rm "app/src/main/java/{{ cookiecutter.package_dir }}/data/local/db/AppDatabase.kt"
        rm "app/src/main/java/{{ cookiecutter.package_dir }}/data/local/entity/DummyEntity.kt"
        echo "Removed Room DB files"
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

launch_studio() {
    if [[ "$OSTYPE" == "darwin"* ]]; then
        /Applications/Android\ Studio.app/Contents/MacOS/studio .
        echo "Android Studio should now be running, have fun with your new project!"
    else
        echo "Unsupported operating system: skipping the launching of Android Studio..."
    fi
}

initialize_git
update_project_permissions
setup_static_code_analysis
remove_unwanted_files
attempt_to_launch_studio