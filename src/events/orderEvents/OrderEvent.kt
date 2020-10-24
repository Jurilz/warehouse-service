package com.warehouseservice.events.orderEvents

import com.warehouseservice.events.Event

class OrderEvent(
    val orderId: String,
    val productName: String,
    val amount: Int,
    val customerName: String,
    val address: String,
    val state: String
): Event()

fun OrderEvent.buildOrderEvent(state: String): OrderEvent {
    return OrderEvent(
        orderId = this.orderId,
        productName = this.productName,
        amount = this.amount,
        customerName = this.customerName,
        address = this.address,
        state = state
    )
}