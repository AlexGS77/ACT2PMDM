package com.example.acteval2pmdm

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity para agregar o editar un cliente
 */
class FormularioClienteActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: Button
    private lateinit var databaseHelper: DatabaseHelper

    private var clienteId: Int = 0
    private var esEdicion: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_cliente)

        inicializarVistas()
        cargarDatosCliente()

        btnGuardar.setOnClickListener {
            guardarCliente()
        }
    }

    /**
     * Inicializar las vistas y la base de datos
     */
    private fun inicializarVistas() {
        etNombre = findViewById(R.id.etNombre)
        etEmail = findViewById(R.id.etEmail)
        etTelefono = findViewById(R.id.etTelefono)
        btnGuardar = findViewById(R.id.btnGuardar)
        databaseHelper = DatabaseHelper(this)
    }

    /**
     * Cargar datos del cliente si es edición
     */
    @SuppressLint("SetTextI18n")
    private fun cargarDatosCliente() {
        clienteId = intent.getIntExtra("ID", 0)

        if (clienteId != 0) {
            esEdicion = true
            title = "Editar Cliente"

            etNombre.setText(intent.getStringExtra("NOMBRE"))
            etEmail.setText(intent.getStringExtra("EMAIL"))
            etTelefono.setText(intent.getStringExtra("TELEFONO"))

            btnGuardar.text = "Actualizar"
        } else {
            esEdicion = false
            title = "Nuevo Cliente"
            btnGuardar.text = "Guardar"
        }
    }

    /**
     * Validar y guardar el cliente
     */
    private fun guardarCliente() {
        val nombre = etNombre.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        // Validar campos
        if (!validarCampos(nombre, email, telefono)) {
            return
        }

        val cliente = Cliente(
            id = clienteId,
            nombre = nombre,
            email = email,
            telefono = telefono
        )

        val resultado = if (esEdicion) {
            databaseHelper.actualizarCliente(cliente).toLong()
        } else {
            databaseHelper.insertarCliente(cliente)
        }

        if (resultado > 0) {
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "Error al guardar el cliente", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Validar todos los campos del formulario
     * @return true si todos los campos son válidos
     */
    private fun validarCampos(nombre: String, email: String, telefono: String): Boolean {
        // Validar nombre
        if (nombre.isEmpty()) {
            etNombre.error = "El nombre es obligatorio"
            etNombre.requestFocus()
            return false
        }

        // Validar email
        if (email.isEmpty()) {
            etEmail.error = "El email es obligatorio"
            etEmail.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Formato de email inválido"
            etEmail.requestFocus()
            return false
        }

        // Validar teléfono
        if (telefono.isEmpty()) {
            etTelefono.error = "El teléfono es obligatorio"
            etTelefono.requestFocus()
            return false
        }

        if (telefono.length < 9) {
            etTelefono.error = "El teléfono debe tener al menos 9 dígitos"
            etTelefono.requestFocus()
            return false
        }

        // Verificar que solo contenga números
        if (!telefono.matches(Regex("^[0-9]+$"))) {
            etTelefono.error = "El teléfono solo puede contener números"
            etTelefono.requestFocus()
            return false
        }

        return true
    }
}