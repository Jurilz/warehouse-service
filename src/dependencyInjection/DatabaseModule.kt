package com.warehouseservice.dependencyInjection

import com.warehouseservice.Arguments
import com.warehouseservice.utils.loadDockerSecrets
import com.warehouseservice.utils.readDockerSecret
import org.jetbrains.exposed.sql.Database
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val databaseModule = module {
    single {
        KMongo
            .createClient("mongodb://${Arguments.mongoHost}:${Arguments.mongoPort}")
//            .createClient()
            .coroutine
            .getDatabase(Arguments.eventDatabaseName)


    }

    single {
        Database
            .connect(
                url = "jdbc:postgresql://${Arguments.postgresHost}:${Arguments.postgresPort}/${Arguments.productDatabaseName}",
                driver = "org.postgresql.Driver",
                user = loadDockerSecrets(Arguments.warehouseSecret)["POSTGRES_USER"]!!,
                password =  loadDockerSecrets(Arguments.warehouseSecret)["POSTGRES_PASSWORD"]!!)
//                user = Arguments.postgresUser,
//                password = Arguments.postgresPassword)
//                user = readDockerSecret("postgresUser")!!,
//                password = readDockerSecret("postgresPassword")!!)
//            .connect("jdbc:postgresql://localhost:5432/products", driver = "org.postgresql.Driver",
//                user = "postgres", password = "postgres")
    }

}