plugins {
    id("java")
    application
    kotlin("jvm")
    kotlin("kapt")
}

application {
    mainClassName = "MyMainClassKt"
}

dependencies {
    implementation(project(":common"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4-M2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")
}
