import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.sentry)
    alias(libs.plugins.gms)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.ray.template.android"
    compileSdk = libs.versions.sdk.compile.get().toInt()

    defaultConfig {
        applicationId = "com.ray.template.android"
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = libs.versions.app.versioncode.get().toInt()
        versionName = libs.versions.app.versionname.get()

        manifestPlaceholders["sentryDsnToken"] = getLocalProperty("SENTRY_DSN_TOKEN")
        manifestPlaceholders["kakaoAppKey"] = getLocalProperty("KAKAO_APP_KEY")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue ("string", "key_kakao_app", getLocalProperty("KAKAO_APP_KEY"))
            manifestPlaceholders["sentryEnvironment"] = "release" + manifestPlaceholders["sentryEnvironment"].toString()
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue ("string", "key_kakao_app", getLocalProperty("KAKAO_APP_KEY"))
            manifestPlaceholders["sentryEnvironment"] = "debug" + manifestPlaceholders["sentryEnvironment"].toString()
        }
    }

    flavorDimensions += "server"
    productFlavors {
        create("development") {
            dimension = "server"
            manifestPlaceholders["sentryEnvironment"] = "development" + manifestPlaceholders["sentryEnvironment"].toString()
        }
        create("production") {
            dimension = "server"
            manifestPlaceholders["sentryEnvironment"] = "production" + manifestPlaceholders["sentryEnvironment"].toString()
        }
    }

    /**
     * Android 14 JDK 17 지원
     * url : https://developer.android.com/about/versions/14/behavior-changes-14?hl=ko#core-libraries
     */
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    testFixtures {
        enable = true
    }
}


sentry {
    // Disables or enables debug log output, e.g. for for sentry-cli.
    // Default is disabled.
    debug = false

    // The slug of the Sentry organization to use for uploading proguard mappings/source contexts.
    org = "ray-sample"

    // The slug of the Sentry project to use for uploading proguard mappings/source contexts.
    projectName = "ray-sample-android"

    // The authentication token to use for uploading proguard mappings/source contexts.
    // WARNING: Do not expose this token in your build.gradle files, but rather set an environment
    // variable and read it into this property.
    authToken = getLocalProperty("SENTRY_AUTH_TOKEN")

    // The url of your Sentry instance. If you're using SAAS (not self hosting) you do not have to
    // set this. If you are self hosting you can set your URL here
    url = "https://sentry.io"

    // Disables or enables the handling of Proguard mapping for Sentry.
    // If enabled the plugin will generate a UUID and will take care of
    // uploading the mapping to Sentry. If disabled, all the logic
    // related to proguard mapping will be excluded.
    // Default is enabled.
    includeProguardMapping = true

    // Whether the plugin should attempt to auto-upload the mapping file to Sentry or not.
    // If disabled the plugin will run a dry-run and just generate a UUID.
    // The mapping file has to be uploaded manually via sentry-cli in this case.
    // Default is enabled.
    autoUploadProguardMapping = true

    // Experimental flag to turn on support for GuardSquare's tools integration (Dexguard and External Proguard).
    // If enabled, the plugin will try to consume and upload the mapping file produced by Dexguard and External Proguard.
    // Default is disabled.
    dexguardEnabled = false
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation(libs.bundles.kotlin)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    implementation(libs.bundles.network)

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.kakao)
    implementation(libs.bundles.logging)
    debugImplementation(libs.leakcanary)

    testImplementation(testFixtures(project(":app")))
    testImplementation(testFixtures(project(":data")))
    testImplementation(testFixtures(project(":domain")))
    testImplementation(testFixtures(project(":presentation")))

    testImplementation(libs.bundles.test)
    kspTest(libs.hilt.android.compiler)

    testFixturesImplementation(libs.bundles.kotlin)
    testFixturesImplementation(libs.hilt.android)
    testFixturesImplementation(libs.bundles.test)
    // TODO : Ksp / Kapt Test Fixtures Update 대기중
    kspTestFixtures(libs.hilt.android.compiler)
}

fun getLocalProperty(propertyKey: String): String {
    return gradleLocalProperties(rootDir, providers).getProperty(propertyKey) ?: System.getenv(propertyKey)
}
