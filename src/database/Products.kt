package com.warehouseservice.database

import org.jetbrains.exposed.sql.Table

object Products: Table() {
    val name = varchar("name", 50)
    val items = integer("items")
    val lastModified = varchar("lastModified", 70)
    override val primaryKey = PrimaryKey(name)
}