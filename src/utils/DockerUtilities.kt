package com.warehouseservice.utils

import com.cars.framework.secrets.DockerSecretLoadException
import com.cars.framework.secrets.DockerSecrets


fun loadDockerSecrets(filename: String? = null): Map<String, String> {
    return try {
        when (filename) {
            null -> DockerSecrets.load()
            else -> DockerSecrets.loadFromFile(filename)
        }
    } catch (e: DockerSecretLoadException) {
        emptyMap()
    }
}

fun readDockerSecret(name: String): String? {
    return loadDockerSecrets()[name]
}