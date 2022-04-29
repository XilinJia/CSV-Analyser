/*
 * Project: <<projectname>>
 * Copyright (c) 2022 Xilin Jia <https://github.com/XilinJia>
 * All rights reserved.
 * This software is released under the MIT license
 * https://opensource.org/licenses/MIT
 */

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.1.1"
}

group = "me.djia"
version = "1.0"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                implementation("com.github.Tlaster.KFilePicker:KFilePicker:1.0.4")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        javaHome = System.getenv("JDK_18")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "CSVAnalyser"
            packageVersion = "1.0.0"
            description = "Multiplatform CSV data analyser"
            copyright = "©2022 Xilin Jia. All rights reserved."
            vendor = "Xilin Jia"
        }
    }
}