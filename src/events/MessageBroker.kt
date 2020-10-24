package com.warehouseservice.events

import com.warehouseservice.commands.Command

interface MessageBroker {

    fun subscribe(topic: MessageTopic, subscriber: Subscriber)

    suspend fun publish(topic: MessageTopic, event: Event)

    suspend fun publishCommand(topic: MessageTopic, command: Command)

    fun getOrCreateSubscriberList(topic: MessageTopic): ArrayList<Subscriber>
}