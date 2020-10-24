package com.warehouseservice.dependencyInjection

import com.warehouseservice.projectors.DefaultProductProjector
import com.warehouseservice.projectors.ProductProjector
import org.koin.dsl.module

val projectorModule = module {
    single<ProductProjector> {
        DefaultProductProjector(get(), get())
    }
}