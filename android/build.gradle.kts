/*
 * Project: CSVAnalyser
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

import java.util.Properties
import java.io.FileInputStream

plugins {
    id("org.jetbrains.compose") version "1.1.1"
    id("com.android.application")
    kotlin("android")
}

val keystorePropertiesFile = rootProject.file("signing.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

group = "me.djia"
version = "1.0"

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("com.github.Tlaster.KFilePicker:KFilePicker:1.0.4")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "me.djia.csvanalyser"
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
        setProperty("archivesBaseName", "CSVAnalyser-" + defaultConfig.versionName)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"]!!)
            storePassword = keystoreProperties["storePassword"] as String
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            manifestPlaceholders["appName"] = "CSVAnalyser"
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
            manifestPlaceholders["appName"] = "CSVAnalyser-D"
        }
    }
}