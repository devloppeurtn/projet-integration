plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Activation du plugin kapt
    id("kotlin-parcelize")

    kotlin("plugin.serialization") version "1.8.0" // Ensure both Kotlin and the serialization plugin are updated
}

android {
    namespace = "com.example.moviefront"
    compileSdk = 34

    viewBinding {
        enable = true
    }

    // Activez DataBinding
    dataBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "com.example.moviefront"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Exclure les fichiers de licence courants
            excludes += "META-INF/androidx.cardview_cardview.version" // Résoudre ce conflit spécifique
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Dépendances de Glide
    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation(libs.play.services.maps)
    implementation(libs.cardview.v7) // Mise à jour de Glide
    kapt("com.github.bumptech.glide:compiler:4.15.0") // Assurez-vous que kapt est activé

    // Autres dépendances
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("io.ktor:ktor-client-core:2.3.0")
    implementation("io.ktor:ktor-client-cio:2.3.0")
    implementation("io.ktor:ktor-client-json:2.3.0")
    implementation("io.ktor:ktor-client-logging:2.3.0")
    implementation("io.ktor:ktor-client-serialization:2.3.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("io.coil-kt:coil:2.1.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("io.ktor:ktor-client-serialization:2.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt ("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.squareup.picasso:picasso:2.8")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.vectordrawable:vectordrawable:1.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Si dans un projet Android
    implementation("androidx.databinding:databinding-runtime:8.7.2")
// Pour la sérialisation JSON avec Kotlin
}
