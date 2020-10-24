package com.warehouseservice.events

import com.warehouseservice.commands.Command


class DefaultMessageBroker: MessageBroker {

    private val subscriberMap: HashMap<MessageTopic, ArrayList<Subscriber>> = HashMap()

    override fun subscribe(topic: MessageTopic, subscriber: Subscriber) {
        val subscriberList: ArrayList<Subscriber> = getOrCreateSubscriberList(topic)
        subscriberList.add(subscriber)
    }

    override suspend fun publish(topic: MessageTopic, event: Event) {
        val subscribers: ArrayList<Subscriber>? = subscriberMap[topic]
        if (subscribers != null) {
            for (subscriber: Subscriber in subscribers) {
                subscriber.handle(event)
            }
        }
    }

    override suspend fun publishCommand(topic: MessageTopic, command: Command) {
        val subscribers: ArrayList<Subscriber>? = subscriberMap[topic]
        if (subscribers != null) {
            for (subscriber: Subscriber in subscribers) {
                subscriber.handleCommand(command)
            }
        }
    }

    override fun getOrCreateSubscriberList(topic: MessageTopic): ArrayList<Subscriber> {
        if (subscriberMap[topic] == null) {
            subscriberMap[topic] = ArrayList()
        }
        return subscriberMap[topic]!!
    }
}