package com.warehouseservice.dependencyInjection

import com.warehouseservice.Arguments
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
            .createClient("mongodb://${Arguments.mongoHost}:${Arguments.mongoPort}")
            .coroutine
            .getDatabase(Arguments.eventDatabaseName)
//            .createClient("mongodb://mongo:27017")
//            .createClient()

//            .getDatabase("palletEvents")

    }

    single {
        Database
            .connect(
                url = "jdbc:postgresql://${Arguments.postgresHost}:${Arguments.postgresPort}/${Arguments.productDatabaseName}",
                driver = "org.postgresql.Driver",
                user = Arguments.postgresUser,
                password = Arguments.postgresPassword)
//            .connect("jdbc:postgresql://postgres:5432/products", driver = "org.postgresql.Driver",
//                user = "postgres", password = "postgres")
//            .connect("jdbc:postgresql://localhost:5432/products", driver = "org.postgresql.Driver",
//                user = "postgres", password = "postgres")
    }

}