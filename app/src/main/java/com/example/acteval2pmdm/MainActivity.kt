package com.example.acteval2pmdm

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Activity principal que muestra la lista de clientes
 */
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClienteAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var fabAgregar: FloatingActionButton
    private lateinit var etBuscar: EditText
    private lateinit var tvContador: TextView
    private var listaClientes = listOf<Cliente>()

    companion object {
        const val REQUEST_CODE_AGREGAR = 1
        const val REQUEST_CODE_EDITAR = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        configurarRecyclerView()
        configurarBusqueda()
        cargarClientes()
    }

    /**
     * Inicializar todas las vistas
     */
    private fun inicializarVistas() {
        recyclerView = findViewById(R.id.recyclerViewClientes)
        fabAgregar = findViewById(R.id.fabAgregar)
        etBuscar = findViewById(R.id.etBuscar)
        tvContador = findViewById(R.id.tvContador)
        databaseHelper = DatabaseHelper(this)

        fabAgregar.setOnClickListener {
            val intent = Intent(this, FormularioClienteActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_AGREGAR)
        }
    }

    /**
     * Configurar el RecyclerView con su adaptador
     */
    private fun configurarRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ClienteAdapter(
            listaClientes,
            onItemClick = { cliente ->
                // Click normal: editar cliente
                editarCliente(cliente)
            },
            onItemLongClick = { cliente ->
                // Long click: eliminar cliente
                mostrarDialogoEliminar(cliente)
                true
            }
        )

        recyclerView.adapter = adapter
    }

    /**
     * Configurar la búsqueda en tiempo real
     */
    private fun configurarBusqueda() {
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buscarClientes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    /**
     * Cargar todos los clientes desde la base de datos
     */
    private fun cargarClientes() {
        listaClientes = databaseHelper.obtenerTodosLosClientes()
        adapter.actualizarLista(listaClientes)
        actualizarContador()
    }

    /**
     * Buscar clientes por nombre o email
     */
    private fun buscarClientes(query: String) {
        listaClientes = if (query.isEmpty()) {
            databaseHelper.obtenerTodosLosClientes()
        } else {
            databaseHelper.buscarClientes(query)
        }
        adapter.actualizarLista(listaClientes)
        actualizarContador()
    }

    /**
     * Actualizar el contador de clientes
     */
    @SuppressLint("SetTextI18n")
    private fun actualizarContador() {
        val total = databaseHelper.contarClientes()
        tvContador.text = "Total de clientes: $total"
    }

    /**
     * Abrir formulario para editar un cliente
     */
    private fun editarCliente(cliente: Cliente) {
        val intent = Intent(this, FormularioClienteActivity::class.java).apply {
            putExtra("ID", cliente.id)
            putExtra("NOMBRE", cliente.nombre)
            putExtra("EMAIL", cliente.email)
            putExtra("TELEFONO", cliente.telefono)
        }
        startActivityForResult(intent, REQUEST_CODE_EDITAR)
    }

    /**
     * Mostrar diálogo de confirmación antes de eliminar
     */
    private fun mostrarDialogoEliminar(cliente: Cliente) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cliente")
            .setMessage("¿Estás seguro de que deseas eliminar a ${cliente.nombre}?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                eliminarCliente(cliente)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    /**
     * Eliminar un cliente de la base de datos
     */
    private fun eliminarCliente(cliente: Cliente) {
        val resultado = databaseHelper.eliminarCliente(cliente.id)
        if (resultado > 0) {
            Toast.makeText(this, "Cliente eliminado correctamente", Toast.LENGTH_SHORT).show()
            cargarClientes()
        } else {
            Toast.makeText(this, "Error al eliminar el cliente", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Recibir el resultado de las activities de agregar/editar
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_AGREGAR -> {
                    Toast.makeText(this, "Cliente agregado correctamente", Toast.LENGTH_SHORT).show()
                    cargarClientes()
                }
                REQUEST_CODE_EDITAR -> {
                    Toast.makeText(this, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
                    cargarClientes()
                }
            }
        }
    }

    /**
     * Recargar clientes al volver a la activity
     */
    override fun onResume() {
        super.onResume()
        cargarClientes()
    }
}