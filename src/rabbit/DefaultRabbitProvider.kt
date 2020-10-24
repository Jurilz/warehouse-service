package com.warehouseservice.rabbit


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rabbitmq.client.*
import com.warehouseservice.events.Event
import com.warehouseservice.events.MessageTopic
import com.warehouseservice.events.MessageBroker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import com.warehouseservice.commands.Command
import com.warehouseservice.events.palletEvents.PalletCreatedEvent
import com.warehouseservice.events.palletEvents.PalletDeletedEvent
import com.warehouseservice.events.palletEvents.PalletUpdatedEvent

class DefaultRabbitProvider(private val messageBroker: MessageBroker): RabbitProvider {

    private val mapper: ObjectMapper = jacksonObjectMapper()
    private val connectionFactory: ConnectionFactory = ConnectionFactory()
    private val channel: Channel
    private val deliverEventCallback: DeliverCallback
    private val deliverCommandCallback: DeliverCallback
    private val cancelCallback: CancelCallback

    val GENERAL_EXCHANGE = "generalExchange"

    val TYPE_DIRECT = "direct"
    val WAREHOUSE_PALLET_COMMAND_QUEUE = "warehousePalletCommandQueue"
    val WAREHOUSE_ORDER_COMMAND_QUEUE = "warehouseOrderCommandQueue"

    val PALLET_COMMAND_KEY = "palletCommand"
    val WAREHOUSE_PALLET_EVENT_QUEUE = "warehousePalletEventQueue"

    val PALLET_EVENT_KEY = "palletEvent"
    val WAREHOUSE_ORDER_EVENT_QUEUE = "warehouseOrderEventQueue"

    val ORDER_EVENT_KEY = "orderEvent"
    val ORDER_COMMAND_KEY = "orderCommand"

    init {
        //TODO: look at this - env ?
        connectionFactory.host = "rabbitmq"
        val newConnection: Connection = this.connectionFactory.newConnection()
        channel = newConnection.createChannel()
        channel.exchangeDeclare(GENERAL_EXCHANGE, TYPE_DIRECT, true)

        deliverEventCallback = DeliverCallback { consumerTag: String?,
                                                 message: Delivery? ->
            val event: Event = mapper.readValue(message!!.body)
            println("Consuming Message \n ${String(message.body)}")
            GlobalScope.async {
                messageBroker.publish(MessageTopic.PALLET_EVENT, event)
            }
        }

        deliverCommandCallback = DeliverCallback { consumerTag: String?,
                                                 message: Delivery? ->
            val command: Command = mapper.readValue(message!!.body)
            println("Consuming Message \n ${String(message.body)}")
            GlobalScope.async {
                messageBroker.publishCommand(MessageTopic.COMMAND_HANDLER, command)
            }
        }

        cancelCallback = CancelCallback { consumerTag: String? ->
            println("Cancelled .. $consumerTag")
        }
        declareAndBindEventQueues()
        declareAndBindCommandQueue()
    }


    private fun declareAndBindEventQueues() {
        channel.queueDeclare(WAREHOUSE_PALLET_EVENT_QUEUE, true, false, true, emptyMap())
        channel.queueBind(WAREHOUSE_PALLET_EVENT_QUEUE, GENERAL_EXCHANGE, PALLET_EVENT_KEY)
        channel.basicConsume(WAREHOUSE_PALLET_EVENT_QUEUE, true, deliverEventCallback, cancelCallback)

        channel.queueDeclare(WAREHOUSE_ORDER_EVENT_QUEUE, true, false, true, emptyMap())
        channel.queueBind(WAREHOUSE_ORDER_EVENT_QUEUE, GENERAL_EXCHANGE, ORDER_EVENT_KEY)
        channel.basicConsume(WAREHOUSE_ORDER_EVENT_QUEUE, true, deliverEventCallback, cancelCallback)
    }

    private fun declareAndBindCommandQueue() {
        channel.queueDeclare(WAREHOUSE_ORDER_COMMAND_QUEUE, true, false, true, emptyMap())
        channel.queueBind(WAREHOUSE_ORDER_COMMAND_QUEUE, GENERAL_EXCHANGE, ORDER_COMMAND_KEY)
        channel.basicConsume(WAREHOUSE_ORDER_COMMAND_QUEUE, true, deliverCommandCallback, cancelCallback)
    }

    override suspend fun handle(event: Event) {
       when(event) {
           is PalletCreatedEvent -> publishPalletEvent(event)
           is PalletUpdatedEvent -> publishPalletEvent(event)
           is PalletDeletedEvent -> publishPalletEvent(event)
           else -> publishOrderEvent(event)
       }
    }

    override suspend fun handleCommand(command: Command) {
        withContext(Dispatchers.IO) {
            channel.basicPublish(
                GENERAL_EXCHANGE,
                ORDER_COMMAND_KEY,
                null,
                mapper.writeValueAsBytes(command)
            )
        }
    }

    private suspend fun publishOrderEvent(event: Event) {
        withContext(Dispatchers.IO) {
            channel.basicPublish(
                GENERAL_EXCHANGE,
                ORDER_EVENT_KEY,
                null,
                mapper.writeValueAsBytes(event)
            )
        }
    }

    private suspend fun publishPalletEvent(event: Event) {
        withContext(Dispatchers.IO) {
            channel.basicPublish(
                GENERAL_EXCHANGE,
                PALLET_EVENT_KEY,
                null,
                mapper.writeValueAsBytes(event)
            )
        }
    }

}