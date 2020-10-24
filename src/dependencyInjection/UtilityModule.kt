package com.warehouseservice.dependencyInjection

import com.warehouseservice.utils.DefaultPalletUtility
import com.warehouseservice.utils.DefaultWarehouseState
import com.warehouseservice.utils.PalletUtility
import com.warehouseservice.utils.WarehouseState
import org.koin.dsl.module

val utilityModule = module {
    single<PalletUtility> {
        DefaultPalletUtility(get(), get())
    }

    single<WarehouseState> {
        DefaultWarehouseState()
    }
}