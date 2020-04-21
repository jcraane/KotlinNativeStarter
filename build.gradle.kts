import java.net.URI

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0")
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
    }
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}