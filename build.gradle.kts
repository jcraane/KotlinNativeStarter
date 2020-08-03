import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.0-alpha07")
        classpath(kotlin("gradle-plugin", version = "1.4-M2"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = URI.create("https://dl.bintray.com/kotlin/ktor")
        }
        maven { url = uri("https://kotlin.bintray.com/kotlinx") }
        maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}