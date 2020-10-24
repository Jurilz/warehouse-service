package com.warehouseservice.routing

import com.warehouseservice.services.ProductService
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import org.koin.ktor.ext.inject

fun Routing.productRoutes() {

    val productService: ProductService by inject()

    route("/products") {
        get {
            call.respond(productService.getAll())
        }
    }
}

