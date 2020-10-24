package com.warehouseservice

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import com.fasterxml.jackson.databind.*
import com.warehouseservice.commands.CommandHandler
import com.warehouseservice.dependencyInjection.*
import com.warehouseservice.events.EventHandler
import com.warehouseservice.events.MessageTopic
import com.warehouseservice.events.MessageBroker
import com.warehouseservice.rabbit.RabbitProvider
import com.warehouseservice.routing.palletRoutes
import com.warehouseservice.routing.productRoutes
import com.warehouseservice.services.PalletService
import com.warehouseservice.services.ProductService
import io.ktor.jackson.*
import io.ktor.features.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        install(Koin) {
            modules(
                databaseModule +
                        eventBrokerModule +
                        handlerModule +
                        rabbitModule +
                        repositoryModule +
                        serviceModule +
                        projectorModule +
                        utilityModule)
        }
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
        anyHost()
    }

    val client = HttpClient(Apache) {
    }

    install(DoubleReceive)

    initialize()

    routing {
        palletRoutes()
        productRoutes()
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

fun Application.initialize() {
    val messageBroker: MessageBroker by inject()
    val rabbitMQProvider: RabbitProvider by inject()
    val eventHandler: EventHandler by inject()
    val palletService: PalletService by inject()
    val productService: ProductService by inject()
    val commandHandler: CommandHandler by inject()
    messageBroker.subscribe(MessageTopic.COMMAND_HANDLER, commandHandler)
    messageBroker.subscribe(MessageTopic.PALLET_SERVICE, palletService)
    messageBroker.subscribe(MessageTopic.PRODUCT_SERVICE, productService)
    messageBroker.subscribe(MessageTopic.PALLET_EVENT, eventHandler)
    messageBroker.subscribe(MessageTopic.PUBLISHER, rabbitMQProvider)
    GlobalScope.async {
        palletService.initializeService()
    }
}


