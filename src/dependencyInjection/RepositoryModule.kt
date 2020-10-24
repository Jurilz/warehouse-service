package com.warehouseservice.dependencyInjection

import com.warehouseservice.repository.DefaultEventWriteRepository
import com.warehouseservice.repository.EventWriteRepository
import com.warehouseservice.repository.PostgresProductReadRepository
import com.warehouseservice.repository.ProductReadRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<EventWriteRepository> {
        DefaultEventWriteRepository(get())
    }

    single<ProductReadRepository> {
        PostgresProductReadRepository(get())
    }
}