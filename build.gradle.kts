group = "me.takagi.sokujichan"

object ThirdpartyVersion {
    const val Ktor = "1.5.1"
    const val KotlinCssJvm = "1.0.0-pre.86-kotlin-1.3.50"

    const val JDA = "4.2.0_168"
    const val JDAUtilities = "3.0.5"

    const val KotlinLogging = "2.0.4"
    const val Logback = "1.2.3"
    const val jansi = "1.18"

    const val CommonsLang = "3.3.1"

    const val JUnit = "5.7.0"
}

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
    // Ktor Server
    implementation("io.ktor:ktor-server-cio:${ThirdpartyVersion.Ktor}")
    implementation( "io.ktor:ktor-server-core:${ThirdpartyVersion.Ktor}")

    // HTML
    implementation( "io.ktor:ktor-html-builder:${ThirdpartyVersion.Ktor}")
    implementation( "org.jetbrains:kotlin-css-jvm:${ThirdpartyVersion.KotlinCssJvm}")

    // JDA
    implementation("net.dv8tion:JDA:${ThirdpartyVersion.JDA}")
    implementation("com.jagrosh:jda-utilities:${ThirdpartyVersion.JDAUtilities}")

    // Logging
    implementation("io.github.microutils:kotlin-logging:${ThirdpartyVersion.KotlinLogging}")
    implementation("ch.qos.logback:logback-core:${ThirdpartyVersion.Logback}")
    implementation("ch.qos.logback:logback-classic:${ThirdpartyVersion.Logback}")
    implementation("org.fusesource.jansi:jansi:${ThirdpartyVersion.jansi}")

    // Util
    implementation("org.apache.commons:commons-lang3:${ThirdpartyVersion.CommonsLang}")

    // Logging
    implementation("io.github.microutils:kotlin-logging:${ThirdpartyVersion.KotlinLogging}")
    implementation("ch.qos.logback:logback-core:${ThirdpartyVersion.Logback}")
    implementation("ch.qos.logback:logback-classic:${ThirdpartyVersion.Logback}")
    implementation("org.fusesource.jansi:jansi:${ThirdpartyVersion.jansi}")

    // Testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:${ThirdpartyVersion.JUnit}")
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


tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    manifest {
        attributes("Main-Class" to "me.takagi.sokujichan.MainKt")
    }
}

task<JavaExec>("run") {
    dependsOn("build")

    group = "application"
    main = "me.takagi.sokujichan.MainKt"
    classpath(configurations.runtimeClasspath, tasks.jar)
}