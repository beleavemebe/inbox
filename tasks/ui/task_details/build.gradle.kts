plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
    id(Plugins.safeArgs)
}

android {
    compileSdk = Build.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = Build.MIN_SDK_VERSION
        targetSdk = Build.TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
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
    implementation(project(Modules.common))
    implementation(project(Modules.Core.di))
    implementation(project(Modules.Core.ui))
    implementation(project(Modules.Core.utils))
    implementation(project(Modules.Core.navigation))
    implementation(project(Modules.TasksFeature.domain))

    implementation(AdapterDelegates.adapterDelegates)

    implementation(Coroutines.coroutinesCore)
    implementation(Coroutines.coroutinesAndroid)

    implementation(Androidx.lifecycleViewmodel)
    implementation(Androidx.lifecycleRuntime)

    implementation(Navigation.navigationUi)
    implementation(Navigation.navigationFragment)

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
