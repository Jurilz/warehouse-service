package com.warehouseservice.events

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.*
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.warehouseservice.events.orderEvents.*
import com.warehouseservice.events.palletEvents.PalletCreatedEvent
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent
import com.warehouseservice.utils.Util
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    Type(value = PalletEvent::class, name = "PalletEvent"),
    Type(value = PalletCreatedEvent::class, name = "PalletCreatedEvent"),
    Type(value = PalletUpdatedEvent::class, name = "PalletUpdatedEvent"),
    Type(value = PalletDeletedEvent::class, name = "PalletDeletedEvent"),
    Type(value = OrderEvent::class, name = "OrderEvent"),
    Type(value = OrderCreatedEvent::class, name = "OrderCreatedEvent"),
    Type(value = OrderUpdatedEvent::class, name = "OrderUpdatedEvent"),
    Type(value = OrderDeletedEvent::class, name = "OrderDeletedEvent"),
    Type(value = OrderCanceledEvent::class, name = "OrderCanceledEvent")
)
abstract class Event {
    var eventId: String = (UUID.randomUUID()).toString()
    var lastModified: String = Util.getCurrentDate()
}