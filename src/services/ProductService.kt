package com.warehouseservice.services

import com.warehouseservice.domain.Product
import com.warehouseservice.events.Event
import com.warehouseservice.events.Subscriber

interface ProductService: Subscriber {

    override suspend fun handle(event: Event)

    suspend fun getAll(): List<Product>
}