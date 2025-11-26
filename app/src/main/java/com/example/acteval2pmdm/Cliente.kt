package com.example.acteval2pmdm

/**
 * Clase de modelo que representa un Cliente
 * @property id Identificador único del cliente (clave primaria)
 * @property nombre Nombre completo del cliente
 * @property email Correo electrónico del cliente
 * @property telefono Número de teléfono del cliente
 */
data class Cliente(
    val id: Int = 0,
    val nombre: String,
    val email: String,
    val telefono: String
)