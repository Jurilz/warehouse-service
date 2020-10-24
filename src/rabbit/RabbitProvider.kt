package com.warehouseservice.rabbit

import com.warehouseservice.events.Event
import com.warehouseservice.events.Subscriber


interface RabbitProvider: Subscriber {

    override suspend fun handle(event: Event)
}