package com.warehouseservice.repository

import com.warehouseservice.database.Products
import com.warehouseservice.domain.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresProductReadRepository (database: Database): ProductReadRepository {

    init {
        transaction {
            SchemaUtils.create(Products)
        }
    }

    override suspend fun getAll(): List<Product> {
        return withContext(Dispatchers.IO) {
            transaction {
                Products.selectAll().toList().map { toProduct(it) }
            }
        }
    }

    override suspend fun getByName(name: String): Product? {
        return withContext(Dispatchers.IO) {
            transaction {
                Products.select {Products.name eq name}
                    .map { toProduct(it) }
                    .firstOrNull()
            }
        }
    }

    suspend fun insert(product: Product) {
        withContext(Dispatchers.IO) {
            transaction {
                Products.insert {
                    it[name] = product.name
                    it[items] = product.items
                    it[lastModified] = product.lastModified
                }
            }
        }
    }

    override suspend fun addOrUpdate(newProduct: Product) {
        val oldProduct: Product? = findOldProduct(newProduct)
        if (oldProduct == null) {
            insert(newProduct)
        } else {
            val updated = Product(
                name = newProduct.name,
                items = newProduct.items + oldProduct.items,
                lastModified = newProduct.lastModified)
            updateProduct(updated)
        }
    }

    override suspend fun removeOrUpdate(removed: Product) {
        val oldProduct: Product? = findOldProduct(removed)
        val oldAmount: Int = oldProduct!!.items
        val newAmount: Int = oldAmount - removed.items
        if (newAmount > 0) {
            oldProduct.items = newAmount
            updateProduct(oldProduct)
        } else {
            delete(oldProduct)
        }
    }

    private fun findOldProduct(removed: Product): Product? {
        return transaction {
            Products.select {Products.name eq removed.name}
                .map { toProduct(it) }
                .firstOrNull()
        }
    }

    override suspend fun delete(product: Product): Int {
        return withContext(Dispatchers.IO) {
            transaction {
                Products.deleteWhere { Products.name eq product.name }
            }
        }
    }

    override suspend fun updateProduct(updated: Product): Int {
        return withContext(Dispatchers.IO) {
            transaction {
                Products.update({ Products.name eq updated.name})  {
                    it[name] = updated.name
                    it[items] = updated.items
                    it[lastModified] = updated.lastModified
                }
            }
        }
    }

    private fun toProduct(row: ResultRow): Product =
        Product(
            name = row[Products.name],
            items = row[Products.items],
            lastModified = row[Products.lastModified]
        )

}

fun <T : Table> T.insertOrUpdate(vararg onDuplicateUpdateKeys: Column<*>, body: T.(InsertStatement<Number>) -> Unit) =
    InsertOrUpdate<Number>(onDuplicateUpdateKeys,this).apply {
        body(this)
        execute(TransactionManager.current())
    }

class InsertOrUpdate<Key : Any>(
    private val onDuplicateUpdateKeys: Array< out Column<*>>,
    table: Table,
    isIgnore: Boolean = false
) : InsertStatement<Key>(table, isIgnore) {
    override fun prepareSQL(transaction: Transaction): String {
        val onUpdateSQL = if(onDuplicateUpdateKeys.isNotEmpty()) {
            " ON DUPLICATE KEY UPDATE " + onDuplicateUpdateKeys.joinToString { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }
        } else ""
        return super.prepareSQL(transaction) + onUpdateSQL
    }
}