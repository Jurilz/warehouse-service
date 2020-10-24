package com.warehouseservice.projectors

import com.warehouseservice.events.Event
import com.warehouseservice.events.palletEvents.PalletCreatedEvent
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent

interface ProductProjector {

//    suspend fun project(events: List<Event>)

//    suspend fun addProducts(palletCreatedEvent: PalletCreatedEvent)

    suspend fun addProducts(palletUpdatedEvent: PalletUpdatedEvent)

    suspend fun removeProducts(palletDeletedEvent: PalletDeletedEvent)

    suspend fun upsertProducts(event: PalletUpdatedEvent)
}