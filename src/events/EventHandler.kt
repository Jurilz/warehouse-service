package com.warehouseservice.events

interface EventHandler: Subscriber {

    override suspend fun handle(event: Event)
}