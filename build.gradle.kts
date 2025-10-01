import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

vkidManifestPlaceholders {
    if (!shouldInjectManifestPlaceholders()) return@vkidManifestPlaceholders
    fun error() = logger.error(
        "Warning! Build will not work!\nCreate the 'secrets.properties' file in the 'app' folder and add your 'VKIDClientID' and 'VKIDClientSecret' to it." +
                "\nFor more information, refer to the 'README.md' file."
    )

    val properties = Properties()
    properties.load(file("app/secrets.properties").inputStream())

    val clientId = properties["VKIDClientID"] ?: error()
    val clientSecret = properties["VKIDClientSecret"] ?: error()

    init(
        clientId = clientId.toString(),
        clientSecret = clientSecret.toString(),
    )
}

/**
 * The project should sync without placeholders
 */
private fun Project.shouldInjectManifestPlaceholders() = gradle
    .startParameter
    .taskNames
    .map { it.lowercase() }
    .any {
        it.contains("assemble")
                || it.endsWith("test")
                || it.contains("lint")
                || it.contains("dokka")
                || it.contains("generatebaselineprofile")
                || it.contains("updatedebugscreenshottest")
                || it.contains("healthmetrics")
    }