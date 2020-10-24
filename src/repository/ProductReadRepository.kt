package com.warehouseservice.repository

import com.warehouseservice.domain.Product


interface ProductReadRepository {

    suspend fun getAll(): List<Product>

    suspend fun getByName(name: String): Product?

    suspend fun addOrUpdate(newProduct: Product)

    suspend fun removeOrUpdate(removed: Product)

    suspend fun delete(product: Product): Int

    suspend fun updateProduct(updated: Product): Int
}