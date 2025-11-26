package com.example.acteval2pmdm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador personalizado para mostrar la lista de clientes en RecyclerView
 */
class ClienteAdapter(
    private var listaClientes: List<Cliente>,
    private val onItemClick: (Cliente) -> Unit,
    private val onItemLongClick: (Cliente) -> Boolean
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    /**
     * ViewHolder que contiene las vistas de cada elemento
     */
    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = listaClientes[position]

        holder.tvNombre.text = cliente.nombre
        holder.tvEmail.text = cliente.email
        holder.tvTelefono.text = cliente.telefono

        // Click normal para editar
        holder.itemView.setOnClickListener {
            onItemClick(cliente)
        }

        // Long click para eliminar
        holder.itemView.setOnLongClickListener {
            onItemLongClick(cliente)
        }
    }

    override fun getItemCount(): Int = listaClientes.size

    /**
     * Actualizar la lista de clientes y refrescar el RecyclerView
     */
    @SuppressLint("NotifyDataSetChanged")
    fun actualizarLista(nuevaLista: List<Cliente>) {
        listaClientes = nuevaLista
        notifyDataSetChanged()
    }
}