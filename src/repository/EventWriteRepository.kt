package com.warehouseservice.repository

import com.warehouseservice.events.Event


interface EventWriteRepository {

    suspend fun insert(event: Event)

    suspend fun getAll(): List<Event>
}