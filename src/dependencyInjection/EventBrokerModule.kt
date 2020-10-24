package com.warehouseservice.dependencyInjection

import com.warehouseservice.events.DefaultMessageBroker
import com.warehouseservice.events.MessageBroker
import org.koin.dsl.module

val eventBrokerModule = module {
    single<MessageBroker> {
        DefaultMessageBroker()
    }
}