plugins {
    kotlin("jvm") version "1.4.10"
}

repositories {
    mavenCentral()
    jcenter()
}

val dyn4jVersion: String = "4.0.0"
val jMonkeyEngineVersion: String = "3.3.2-stable"
val tornadofxVersion = "1.7.20"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.3.9")
    implementation("org.dyn4j", "dyn4j", dyn4jVersion)
    implementation("org.jmonkeyengine", "jme3-core", jMonkeyEngineVersion)
    implementation("org.jmonkeyengine", "jme3-desktop", jMonkeyEngineVersion)
    implementation("org.jmonkeyengine", "jme3-lwjgl", jMonkeyEngineVersion)
    implementation("org.jmonkeyengine", "jme3-plugins", jMonkeyEngineVersion)
    implementation("no.tornado", "tornadofx", tornadofxVersion)
}


group = "io.ramenergy"
version = "1.0-ALPHA"


tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.apply {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }


    register<Jar>("fatJar") {
        dependsOn(getByName("build"))
        destinationDirectory.set(projectDir.resolve("out/artifacts"))
        manifest {
            attributes["Main-Class"] = "launcher.ReadyPlayer42LauncherKt"
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from(sourceSets.main.get().runtimeClasspath.files.filter { it.name.endsWith("jar") }.map { zipTree(it) })
    }
}