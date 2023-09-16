plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.rakshak20.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.rakshak20.android"
        minSdk = 31
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:1.4.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.4.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("androidx.activity:activity-compose:1.7.0")
    var nav_version = "2.6.0"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    //extending design
    implementation("androidx.compose.material:material-icons-extended:1.5.1")
//    graphs
    implementation ("co.yml:ycharts:2.1.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.4.3")
}