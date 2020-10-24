package com.warehouseservice.dependencyInjection

import com.warehouseservice.services.DefaultPalletService
import com.warehouseservice.services.DefaultProductService
import com.warehouseservice.services.PalletService
import com.warehouseservice.services.ProductService
import org.koin.dsl.module

val serviceModule = module {
    single<PalletService> {
        DefaultPalletService(get(), get(), get())
    }

    single<ProductService> {
        DefaultProductService(get(), get())
    }
}