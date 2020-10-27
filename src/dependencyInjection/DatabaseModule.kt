package com.warehouseservice.dependencyInjection

import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
//            .createClient("mongodb://mongo:27017")
            .createClient()
            .coroutine
            .getDatabase("palletEvents")
    }

    single {
        Database
//            .connect("jdbc:postgresql://postgres:5432/products", driver = "org.postgresql.Driver",
//                user = "postgres", password = "postgres")
            .connect("jdbc:postgresql://localhost:5432/products", driver = "org.postgresql.Driver",
                user = "postgres", password = "postgres")
    }

}