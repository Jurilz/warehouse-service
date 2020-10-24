package com.warehouseservice.events.orderEvents

import com.warehouseservice.events.Event

class OrderCreatedEvent(val orderEvent: OrderEvent): Event()