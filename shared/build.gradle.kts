import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "com.ahmad.raza.coinroutine.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.compose.uiTooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.biometric)
        }
        iosMain.dependencies {
            implementation(libs.ktor.ios)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)


            implementation(libs.kotlin.date.time)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.bundles.ktor)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.navigation)

            implementation(libs.coil.compose.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.core)
            implementation(libs.coil.svg)
            implementation(libs.coil.network.ktor)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.test.turbine)
            implementation(libs.test.assertk)
            implementation(libs.ui.test)
        }
    }
}

val apiKey = project.loadLocalProperty(
    "local.properties",
    "API_KEY"
)

buildkonfig {
    packageName = "com.ahmad.raza.coinroutine.shared"

    defaultConfigs {
        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "API_KEY",
            apiKey
        )
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    dependencies {
        add("kspAndroid", libs.room.compiler)
        add("kspIosArm64", libs.room.compiler)
        add("kspIosSimulatorArm64", libs.room.compiler)
        androidRuntimeClasspath(libs.compose.uiTooling)
    }
}

fun Project.loadLocalProperty(
    path: String,
    propertyName: String,
): String {
    val file = rootProject.file(path)

    require(file.exists()) {
        "File '$path' does not exist."
    }

    val properties = Properties().apply {
        load(file.inputStream())
    }

    return properties.getProperty(propertyName)
        ?: throw GradleException(
            "Property '$propertyName' not found in '$path'."
        )
}