package {{ cookiecutter.package_name }}.utils

import java.io.InputStreamReader

class MockResponseFileReader {

    fun readFile(path: String): String {
        val classLoader = this.javaClass.classLoader ?: return ""
        val reader = InputStreamReader(classLoader.getResourceAsStream(path))
        val content = reader.readText()
        reader.close()
        return content
    }
}
