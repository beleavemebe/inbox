plugins {
    id(Plugins.androidApplication)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.safeArgs)
}

android {
    compileSdk = Build.COMPILE_SDK_VERSION

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "io.github.beleavemebe.inbox"
        minSdk = Build.MIN_SDK_VERSION
        targetSdk = Build.TARGET_SDK_VERSION
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = Build.SOURCE_COMPATIBILITY_JAVA_VERSION
        targetCompatibility = Build.TARGET_COMPATIBILITY_JAVA_VERSION
    }
    kotlinOptions {
        jvmTarget = Build.JVM_TARGET_VERSION
    }
}

dependencies {
    implementation(project(Modules.Core.di))
    implementation(project(Modules.Core.diDagger))
    implementation(project(Modules.Core.ui))
    implementation(project(Modules.Core.utils))
    implementation(project(Modules.Core.navigation))

    implementation(project(Modules.TasksFeature.data))
    implementation(project(Modules.TasksFeature.domain))
    implementation(project(Modules.TasksFeature.Ui.library))
    implementation(project(Modules.TasksFeature.Ui.taskDetails))
    implementation(project(Modules.TasksFeature.Ui.taskList))

    implementation(AdapterDelegates.adapterDelegates)

    implementation(Moshi.moshi)
    kapt(Moshi.moshiCodegen)

    implementation(Coroutines.coroutinesCore)
    implementation(Coroutines.coroutinesAndroid)

    implementation(Androidx.lifecycleViewmodel)
    implementation(Androidx.lifecycleRuntime)

    implementation(Navigation.navigationUi)
    implementation(Navigation.navigationFragment)

    implementation(Room.room)
    implementation(Room.roomRuntime)
    kapt(Room.roomCompiler)

    implementation(Dagger.dagger)
    kapt(Dagger.daggerCompiler)

    implementation(ViewBindingDelegate.viewBindingDelegate)

    implementation(Androidx.core)
    implementation(Androidx.appCompat)
    implementation(Google.material)
    implementation(Androidx.constraintLayout)
    testImplementation(Junit.junit)
    androidTestImplementation(Androidx.junit)
    androidTestImplementation(Androidx.espressoCore)
}
