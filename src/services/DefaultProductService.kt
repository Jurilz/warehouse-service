package com.warehouseservice.services

import com.warehouseservice.commands.Command
import com.warehouseservice.commands.orderCommands.ValidateOrderCommand
import com.warehouseservice.domain.OrderState
import com.warehouseservice.domain.Product
import com.warehouseservice.events.Event
import com.warehouseservice.events.MessageTopic
import com.warehouseservice.events.MessageBroker
import com.warehouseservice.events.orderEvents.OrderEvent
import com.warehouseservice.events.orderEvents.OrderUpdatedEvent
import com.warehouseservice.repository.ProductReadRepository

class DefaultProductService(
    private val productReadRepository: ProductReadRepository,
    private val messageBroker: MessageBroker
): ProductService {

    override suspend fun handle(event: Event) {
        TODO("Not yet implemented")
//        when(event) {
//            is OrderCreatedEvent -> validateAndProcessOrder(event)
//        }
    }

    override suspend fun handleCommand(command: Command) {
        when(command) {
            is ValidateOrderCommand -> validateAndProcessOrder(command)
        }
    }


    override suspend fun getAll(): List<Product> {
        return productReadRepository.getAll()
    }

    private suspend fun validateAndProcessOrder(command: ValidateOrderCommand) {
        val product: Product? = productReadRepository.getByName(command.orderCommand.productName)
        if ((product == null) || product.items < command.orderCommand.amount) {
            cancelOrder(command)
        } else {
            approveOrder(command)
        }
    }

    private suspend fun approveOrder(command: ValidateOrderCommand) {
        val approved = OrderEvent(
            orderId = command.orderCommand.orderId,
            productName = command.orderCommand.productName,
            amount = command.orderCommand.amount,
            customerName = command.orderCommand.customerName,
            address = command.orderCommand.address,
            state = OrderState.processedByWarehouse.toString()
        )
        val approvedEvent = OrderUpdatedEvent(approved)
        messageBroker.publish(MessageTopic.PUBLISHER, approvedEvent)
    }

    private suspend fun cancelOrder(command: ValidateOrderCommand) {
        val canceled = OrderEvent(
            orderId = command.orderCommand.orderId,
            productName = command.orderCommand.productName,
            amount = command.orderCommand.amount,
            customerName = command.orderCommand.customerName,
            address = command.orderCommand.address,
            state = OrderState.canceledByWarehouse.toString()
        )
        val canceledEvent = OrderUpdatedEvent(canceled)
        messageBroker.publish(MessageTopic.PUBLISHER, canceledEvent)
    }
}