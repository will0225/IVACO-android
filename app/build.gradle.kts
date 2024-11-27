plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        tasks.withType<JavaCompile> {
            options.compilerArgs.add("-Xlint:deprecation")
        }
    }

    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
    ndkVersion = "25.1.8937393"
}

dependencies {
    // Local libraries
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.navigation:navigation-fragment:2.3.5")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    implementation("androidx.core:core:1.8.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.activity:activity-ktx:1.6.0")  // Or the latest version


    // Testing libraries
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    // RxJava and Coroutines
    implementation("io.reactivex.rxjava3:rxjava:3.1.2")
    implementation("io.reactivex.rxjava2:rxjava:2.2.8")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // Networking libraries
    implementation("com.squareup.okhttp3:okhttp:3.12.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.6.1")
    implementation ("com.squareup.picasso:picasso:2.8") // Add this line
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    // implementation("io.agora.rtc2:agora-rtm-sdk:1.1.0") // If you are also using RTM


  //  implementation ("io.agora.rtc:agora-rtc-sdk:3.6.1") // Check for the latest version
//    implementation ("io.agora.rtc:full-sdk:3.5.0") // This might include the full SDK
//    implementation ("io.github.agoraio-community:VideoUIKit-Android:1.0.0")


    // Gson and utilities
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.apache.commons:commons-lang3:3.8.1")
    implementation("commons-io:commons-io:2.7")
    implementation("com.google.guava:guava:28.0-android")

    // MMKV and Room
    implementation("com.tencent:mmkv-static:1.2.11")
    implementation("androidx.room:room-runtime:2.4.2")
    annotationProcessor("androidx.room:room-compiler:2.4.2")

    // Nordic BLE libraries
    implementation("no.nordicsemi.android:ble:2.5.1")
    implementation("no.nordicsemi.android:log:2.3.0")
    implementation("no.nordicsemi.android:ble-common:2.5.1")
    implementation("no.nordicsemi.android:dfu:2.1.0")
    implementation("no.nordicsemi.android.support.v18:scanner:1.4.3")

    // BP5S and other libraries
    implementation("com.alibaba:fastjson:1.2.48")
    implementation("org.greenrobot:greendao:3.2.2")

    // Kotlin support
    val kotlinVersion = "1.6.0"
    implementation("androidx.core:core-ktx:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // Play Services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Spongy Castle
    api("com.madgag.spongycastle:prov:1.56.0.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}
