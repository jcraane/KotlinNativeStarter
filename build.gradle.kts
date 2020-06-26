import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha02")
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = URI.create("https://dl.bintray.com/kotlin/ktor")
        }
        maven { url = URI.create("https://kotlin.bintray.com/kotlinx") }
        maven { url = URI.create("https://dl.bintray.com/korlibs/korlibs") }
        maven { url = URI.create("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}