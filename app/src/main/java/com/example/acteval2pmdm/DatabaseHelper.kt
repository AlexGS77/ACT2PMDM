package com.example.acteval2pmdm

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Helper para gestionar la base de datos SQLite
 * Implementa un CRUD completo para la tabla de clientes
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CRMClientes.db"
        private const val TABLE_CLIENTES = "clientes"

        // Columnas de la tabla
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_EMAIL = "email"
        private const val KEY_TELEFONO = "telefono"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_CLIENTES ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$KEY_NOMBRE TEXT NOT NULL,"
                + "$KEY_EMAIL TEXT NOT NULL,"
                + "$KEY_TELEFONO TEXT NOT NULL)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        onCreate(db)
    }

    /**
     * Insertar un nuevo cliente
     * @param cliente Objeto Cliente a insertar
     * @return ID del cliente insertado o -1 si hay error
     */
    fun insertarCliente(cliente: Cliente): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE, cliente.nombre)
            put(KEY_EMAIL, cliente.email)
            put(KEY_TELEFONO, cliente.telefono)
        }

        val id = db.insert(TABLE_CLIENTES, null, values)
        db.close()
        return id
    }

    /**
     * Obtener todos los clientes de la base de datos
     * @return Lista de clientes
     */
    fun obtenerTodosLosClientes(): List<Cliente> {
        val listaClientes = mutableListOf<Cliente>()
        val selectQuery = "SELECT * FROM $TABLE_CLIENTES ORDER BY $KEY_NOMBRE ASC"

        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val cliente = Cliente(
                        id = it.getInt(it.getColumnIndexOrThrow(KEY_ID)),
                        nombre = it.getString(it.getColumnIndexOrThrow(KEY_NOMBRE)),
                        email = it.getString(it.getColumnIndexOrThrow(KEY_EMAIL)),
                        telefono = it.getString(it.getColumnIndexOrThrow(KEY_TELEFONO))
                    )
                    listaClientes.add(cliente)
                } while (it.moveToNext())
            }
        }

        db.close()
        return listaClientes
    }

    /**
     * Actualizar un cliente existente
     * @param cliente Objeto Cliente con datos actualizados
     * @return Número de filas afectadas
     */
    fun actualizarCliente(cliente: Cliente): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NOMBRE, cliente.nombre)
            put(KEY_EMAIL, cliente.email)
            put(KEY_TELEFONO, cliente.telefono)
        }

        val resultado = db.update(TABLE_CLIENTES, values, "$KEY_ID = ?", arrayOf(cliente.id.toString()))
        db.close()
        return resultado
    }

    /**
     * Eliminar un cliente
     * @param id ID del cliente a eliminar
     * @return Número de filas eliminadas
     */
    fun eliminarCliente(id: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete(TABLE_CLIENTES, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    /**
     * Buscar clientes por nombre o email
     * @param query Texto de búsqueda
     * @return Lista de clientes que coinciden con la búsqueda
     */
    fun buscarClientes(query: String): List<Cliente> {
        val listaClientes = mutableListOf<Cliente>()
        val selectQuery = "SELECT * FROM $TABLE_CLIENTES WHERE $KEY_NOMBRE LIKE ? OR $KEY_EMAIL LIKE ? ORDER BY $KEY_NOMBRE ASC"

        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery(selectQuery, arrayOf("%$query%", "%$query%"))

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val cliente = Cliente(
                        id = it.getInt(it.getColumnIndexOrThrow(KEY_ID)),
                        nombre = it.getString(it.getColumnIndexOrThrow(KEY_NOMBRE)),
                        email = it.getString(it.getColumnIndexOrThrow(KEY_EMAIL)),
                        telefono = it.getString(it.getColumnIndexOrThrow(KEY_TELEFONO))
                    )
                    listaClientes.add(cliente)
                } while (it.moveToNext())
            }
        }

        db.close()
        return listaClientes
    }

    /**
     * Obtener el número total de clientes
     * @return Cantidad de clientes en la base de datos
     */
    fun contarClientes(): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CLIENTES", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }
}