package me.takagi.sokujichan

import me.takagi.sokujichan.common.Env
import me.takagi.sokujichan.model.Sokuji
import org.bson.Document
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

internal val mongodb: CoroutineClient by lazy {
    if (Env.MONGO_USER != null && Env.MONGO_PASS != null) {
        KMongo.createClient("mongodb://${Env.MONGO_USER}:${Env.MONGO_PASS}@${Env.MONGO_HOST}:${Env.MONGO_PORT}")
    } else {
        KMongo.createClient("mongodb://${Env.MONGO_HOST}:${Env.MONGO_PORT}")
    }.coroutine
}

internal val database: CoroutineDatabase by lazy {
    mongodb.getDatabase("sokujichan")
}

internal val collection: CoroutineCollection<Sokuji> by lazy {
    database.getCollection("sokuji")
}