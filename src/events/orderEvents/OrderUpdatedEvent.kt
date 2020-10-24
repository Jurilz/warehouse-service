package com.warehouseservice.events.orderEvents

import com.warehouseservice.events.Event

class OrderUpdatedEvent(val orderEvent: OrderEvent): Event()