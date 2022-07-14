plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
    id(Plugins.kotlinKapt)
}

android {
    compileSdk = Build.COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = Build.MIN_SDK_VERSION
        targetSdk = Build.TARGET_SDK_VERSION

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(project(Modules.TasksFeature.domain))

    implementation(Dagger.dagger)
    kapt(Dagger.daggerCompiler)

    implementation(Room.room)
    implementation(Room.roomRuntime)
    kapt(Room.roomCompiler)

    implementation(Moshi.moshi)
    kapt(Moshi.moshiCodegen)
}
