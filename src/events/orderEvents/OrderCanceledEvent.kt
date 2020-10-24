package com.warehouseservice.events.orderEvents

import com.warehouseservice.events.Event

class OrderCanceledEvent(val orderEvent: OrderEvent): Event()