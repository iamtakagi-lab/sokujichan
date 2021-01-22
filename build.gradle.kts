group = "me.takagi.sokujichan"
val artifactID = "sokujichan"
val versionMajor = 1
val versionMinor = 0
val versionPatch = 3
version = "${versionMajor}.${versionMinor}.${versionPatch}"
val ktor_version = "1.3.2"
val kotlin_version = "1.4.21"
val logback_version = "1.2.1"

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    mavenLocal()
    jcenter()
    maven ( url =  "https://kotlin.bintray.com/ktor" )
    maven ( url = "https://kotlin.bintray.com/kotlin-js-wrappers" )
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-cio:${ktor_version}")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation( "io.ktor:ktor-server-core:$ktor_version")
    implementation( "io.ktor:ktor-websockets:$ktor_version")
    implementation( "io.ktor:ktor-locations:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation( "io.ktor:ktor-jackson:$ktor_version")
    implementation( "io.ktor:ktor-gson:$ktor_version")
    implementation( "io.ktor:ktor-html-builder:$ktor_version")
    implementation( "org.jetbrains:kotlin-css-jvm:1.0.0-pre.86-kotlin-1.3.50")

    implementation("net.dv8tion:JDA:4.2.0_168")
    implementation("com.jagrosh:jda-utilities:3.0.5")
    implementation("org.apache.commons:commons-lang3:3.1")
    implementation("org.mongodb:mongo-java-driver:3.12.1")
}


kotlin {
    target {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                apiVersion = "1.4"
                languageVersion = "1.4"
                verbose = true
            }
        }
    }

    sourceSets.all {
        languageSettings.progressiveMode = true
        languageSettings.apply {
            useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
            useExperimentalAnnotation("kotlin.io.path.ExperimentalPathApi")
            useExperimentalAnnotation("kotlin.time.ExperimentalTime")
            useExperimentalAnnotation("kotlin.ExperimentalStdlibApi")
        }
    }
}

task<JavaExec>("run") {
    dependsOn("build")

    group = "application"
    main = "me.takagi.sokujichan.MainKt"
    classpath(configurations.runtimeClasspath, tasks.jar)
}