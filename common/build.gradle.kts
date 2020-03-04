
import nl.jamiecraane.buildscript.Localize
import nl.jamiecraane.buildscript.MoveLocalizedFiles
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.3.70")
    }
}

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization") version "1.3.70"
}

repositories {
    mavenCentral()
}

val ktor_version = "1.2.6"
val kotlin_serialization = "0.14.0"
val klockVersion = "1.9.1"

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.70")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.core:core-ktx:1.1.0")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("com.jakewharton.timber:timber:4.7.1")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

val frameworkName = "common"
kotlin {
    android {}

    //    one or combination of: armv7 arm64 x86_64
    val supportedTargets = ((project.findProperty("kotlin.target") ?: "x86_64") as String).split(" ")
    println("targets = $supportedTargets")

    val mapping = mapOf(
        "armv7" to iosArm32("ios32"),
        "arm64" to iosArm64("ios64"),
        "x86_64" to iosX64("iosX64")
    )
    val targets = mapping
        .filter { supportedTargets.contains(it.key) }
        .map { it.value }

    configure(targets) {
        binaries.framework {
            baseName = frameworkName
            embedBitcode = Framework.BitcodeEmbeddingMode.BITCODE
        }
    }

    tasks.create("buildFatFramework", FatFrameworkTask::class) {
        baseName = frameworkName
        val buildType = (project.findProperty("kotlin.build.type") ?: "DEBUG") as String
        from(targets.map { it.binaries.getFramework(buildType) })

        destinationDir = buildDir.resolve("fat-framework/${buildType.toLowerCase()}")
        group = "Universal framework"
        description = "Builds a universal (fat) ${buildType.toLowerCase()} framework"

        doLast {
            val targetDir = findProperty("configuration.build.dir")
                ?: throw RuntimeException("configuration.build.dir is not defined. Please pass this property from the XCode build.")
//             This task attaches native framework built from ios module to Xcode project
            copy {
                from(destinationDir)
                into(targetDir!!)
                include("common.framework/**")
                include("common.framework.dSYM")
            }
        }
    }

    sourceSets["commonMain"].dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("io.ktor:ktor-client-core:$ktor_version")
        implementation("io.ktor:ktor-client-logging:$ktor_version")
        implementation("io.ktor:ktor-client-json:$ktor_version")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$kotlin_serialization")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlin_serialization")
        implementation("com.soywiz.korlibs.klock:klock:$klockVersion")
    }
    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }
    sourceSets["androidMain"].dependencies {
        implementation(kotlin("stdlib"))
        implementation("io.ktor:ktor-client-android:$ktor_version")
        implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
        implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
        api("com.soywiz.korlibs.klock:klock-jvm:$klockVersion")
    }
    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
    }
    sourceSets["ios32Main"].dependencies {
        implementation("com.soywiz.korlibs.klock:klock-iosarm32:$klockVersion")
        implementation("io.ktor:ktor-client-ios-iosarm32:$ktor_version")
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$kotlin_serialization")
    }
    sourceSets["ios64Main"].dependencies {
        implementation("com.soywiz.korlibs.klock:klock-iosarm64:$klockVersion")
        implementation("io.ktor:ktor-client-ios-iosarm64:$ktor_version")
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$kotlin_serialization")
    }
    sourceSets["iosX64Main"].dependencies {
        implementation("com.soywiz.korlibs.klock:klock-iosx64:$klockVersion")
        implementation("io.ktor:ktor-client-ios-iosx64:$ktor_version")
        api("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$kotlin_serialization")
    }
}

val localizationProjectFolder = "localization"
val pathToLocalizeProject = File(gradle.rootProject.projectDir, localizationProjectFolder)
val projectSourceFolder = File(projectDir, "src")
val outputDir = File(projectDir, "generated")

tasks {
    val localize by registering(Localize::class) {
        inputs.dir(pathToLocalizeProject)
        outputs.dir(outputDir)
        outputFolder = outputDir
        repoLocation = pathToLocalizeProject
    }

    val moveLocalizeFiles by registering(MoveLocalizedFiles::class) {
        output = outputDir
        source = projectSourceFolder
        dependsOn(localize)
    }

    val cleanGenerated by registering {
        doLast {
            outputDir.deleteRecursively()
        }
    }

    // This executes the localize task before the build phase
    named("preBuild") {
        dependsOn(moveLocalizeFiles)
    }

    named("clean") {
        dependsOn(cleanGenerated)
    }
}
