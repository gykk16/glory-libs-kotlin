import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "2.0.0"
}

val prjName = "glory-core"
val group = "io.glory"
val version = "0.0.1"

val kotlinLoggingVer = "7.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVer")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
    archiveFileName.set("$prjName.jar")
    dependsOn("fatJar")
}

val fatJarTask = tasks.register<Jar>("fatJar") {
    archiveClassifier = "uber"
    archiveFileName.set("$prjName-$version.jar")

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

val copyJarToLibs = tasks.register("copyJarToLibs") {
    dependsOn(fatJarTask)

    doLast {
        copyJar("../../core/core-mvc/libs")
        copyJar("../../services/demo/libs")
    }
}

tasks.getByName("build") {
    dependsOn(copyJarToLibs)
}

fun copyJar(destination: String) {
    println("Copying jar file to ../services/demo/libs directory...")
    val sourceDir = file("build/libs")
    val destDir = file(destination)

    destDir.mkdirs()

    // Specify the source file
    val sourceFile = sourceDir.resolve("$prjName-$version.jar")

    // Copy the file
    sourceFile.copyTo(destDir.resolve("$prjName-$version.jar"), overwrite = true)

    println("File copied successfully. $sourceFile -> $destDir/$prjName-$version.jar")
}