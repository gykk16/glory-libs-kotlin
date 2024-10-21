import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
}

val prjName = "glory-core-mvc"
val group = "io.glory"
val version = "0.0.1"

val kotlinLoggingVer = "7.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("libs/glory-core-0.0.1.jar"))

    implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVer")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter-cache")

    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-tx")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // caffeine :: cache
    implementation("com.github.ben-manes.caffeine:caffeine")

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("com.h2database:h2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
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

    doLast {
        project.tasks.named("copyJarToLibs").get().run { }
    }
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
        println("Copying jar file to ../services/demo/libs directory...")
        val sourceDir = file("build/libs")
        val destDir = file("../../services/demo/libs")

        destDir.mkdirs()

        // Specify the source file
        val sourceFile = sourceDir.resolve("$prjName-$version.jar")

        // Copy the file
        sourceFile.copyTo(destDir.resolve("$prjName-$version.jar"), overwrite = true)

        println("File copied successfully. $sourceFile -> $destDir/$prjName-$version.jar")
    }
}

tasks.getByName("build") {
    dependsOn(copyJarToLibs)
}