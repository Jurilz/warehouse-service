package com.warehouseservice.dependencyInjection

import com.warehouseservice.commands.CommandHandler
import com.warehouseservice.commands.DefaultCommandHandler
import com.warehouseservice.events.DefaultEventHandler
import com.warehouseservice.events.EventHandler
import org.koin.dsl.module


val handlerModule = module {
    single<CommandHandler> {
        DefaultCommandHandler(get())
    }

    single<EventHandler> {
        DefaultEventHandler(get(), get())
    }
}