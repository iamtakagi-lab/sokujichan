package me.takagi.sokujichan.common

import ch.qos.logback.classic.Level
import mu.KLogger
import mu.KotlinLogging

internal fun KotlinLogging.createLogger(name: String): KLogger {
    val logger = logger(name)
    val underlying = logger.underlyingLogger
    if (underlying is ch.qos.logback.classic.Logger) {
        underlying.level = Level.toLevel(Env.LOG, Level.INFO)
    }
    return logger
}