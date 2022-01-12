package com.example.practica_uno.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.practica_uno.Contador
import com.example.practica_uno.R
import com.example.practica_uno.database.Crud
import com.example.practica_uno.databinding.CardviewlayoutBinding

class AdapterDatos(val lista: MutableList<Datos>) :
    RecyclerView.Adapter<AdapterDatos.DatosHolder>() {
    class DatosHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding = CardviewlayoutBinding.bind(v)
    }

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatosHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.cardviewlayout, parent, false)
        return DatosHolder(v)
    }

    override fun onBindViewHolder(holder: DatosHolder, position: Int) {
        val element = lista[position]
        holder.binding.txtNombre.text = element.nombre
        holder.binding.txtTiempo.text = element.tiempo.toString()
        holder.binding.txtCreacion.text = element.creacion.toString()

        holder.binding.btnIniciar.setOnClickListener {
            val i2 = Intent(context, Contador::class.java).apply {
                putExtra("TIEMPO2", element.tiempo)

            }
            context.startActivity(i2)
        }
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    fun removeAt(position: Int) {
        Crud().delete(lista[position].id)
        lista.removeAt(position)
        notifyItemRemoved(position)
    }

}