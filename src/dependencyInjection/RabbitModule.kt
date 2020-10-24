package com.warehouseservice.dependencyInjection

import com.warehouseservice.rabbit.DefaultRabbitProvider
import com.warehouseservice.rabbit.RabbitProvider
import org.koin.dsl.module

val rabbitModule = module {
    single<RabbitProvider> {
        DefaultRabbitProvider(get())
    }
}