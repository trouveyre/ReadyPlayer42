plugins {
    kotlin("jvm") version "1.4.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

val jMonkeyEngineVersion = "3.3.2-stable"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jmonkeyengine", "jme3-core", jMonkeyEngineVersion)
    implementation("org.jmonkeyengine", "jme3-desktop", jMonkeyEngineVersion)
    implementation("org.jmonkeyengine", "jme3-lwjgl", jMonkeyEngineVersion)
}
