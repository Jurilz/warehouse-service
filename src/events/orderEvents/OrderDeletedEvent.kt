package com.warehouseservice.events.orderEvents

import com.warehouseservice.events.Event

class OrderDeletedEvent(val orderEvent: OrderEvent): Event()