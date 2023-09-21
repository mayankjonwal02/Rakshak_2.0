import java.net.URLEncoder

//import org.jetbrains.kotlin.gradle.internal.kapt.incremental.UnknownSnapshot.classpath

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.1").apply(false)
    id("com.android.library").version("7.4.1").apply(false)
    kotlin("android").version("1.8.10").apply(false)
    kotlin("multiplatform").version("1.8.10").apply(false)

}
allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io")  } // Correct way to specify Maven repository URL
        // Add other repositories if needed
    }
}




tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
