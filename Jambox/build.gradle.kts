plugins {
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

android {
    namespace = "com.jambox.monetisation"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)

    implementation("com.applovin:applovin-sdk:+")
    implementation("com.applovin.mediation:chartboost-adapter:9.7.0.0")
    implementation("com.google.android.gms:play-services-base:16.1.0")
    implementation("com.applovin.mediation:fyber-adapter:+")
    implementation("com.applovin.mediation:google-ad-manager-adapter:+")
    implementation("com.applovin.mediation:google-adapter:22.6.0.0")
    implementation("com.applovin.mediation:inmobi-adapter:10.6.7.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.applovin.mediation:ironsource-adapter:8.0.0.0.0")
    implementation("com.applovin.mediation:vungle-adapter:+")
    implementation("com.applovin.mediation:facebook-adapter:6.16.0.2")
    implementation("com.applovin.mediation:mintegral-adapter:16.7.31.0")
    implementation("com.applovin.mediation:bytedance-adapter:+")
    implementation("com.applovin.mediation:smaato-adapter:22.6.1.0")
    implementation("com.applovin.mediation:unityads-adapter:4.9.3.0")
    implementation("com.applovin.mediation:verve-adapter:3.0.0.0")
    implementation("com.applovin.mediation:yandex-adapter:7.0.1.0")
}

android {
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.jamboxgames"
                artifactId = "monetise-sdk"
                version = "1.0.8"

                from(components["release"])

                //com.github.jamboxgames:monetise-sdk:1.0.8
            }
        }
    }
}