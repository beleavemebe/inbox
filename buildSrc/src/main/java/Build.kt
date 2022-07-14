import org.gradle.api.JavaVersion

object Build {
    const val MIN_SDK_VERSION = 24
    const val TARGET_SDK_VERSION = 32
    const val COMPILE_SDK_VERSION = 32
    const val JVM_TARGET_VERSION = "1.8"
    val SOURCE_COMPATIBILITY_JAVA_VERSION = JavaVersion.VERSION_1_8
    val TARGET_COMPATIBILITY_JAVA_VERSION = JavaVersion.VERSION_1_8
}
