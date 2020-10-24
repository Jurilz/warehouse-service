package com.warehouseservice.repository

import com.warehouseservice.events.Event
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.coroutine.CoroutineDatabase

class DefaultEventWriteRepository(private val database: CoroutineDatabase): EventWriteRepository {

    private val eventCollection: CoroutineCollection<Event> = database.getCollection<Event>()

    override suspend fun insert(event: Event) {
        eventCollection.insertOne(event)
    }

    override suspend fun getAll(): List<Event> {
        return eventCollection.find().toList()
    }
}