package com.example.practica_uno.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica_uno.R
import com.example.practica_uno.databinding.ActivityContadorBinding
import com.example.practica_uno.nasa.NasaGson
import com.squareup.picasso.Picasso

class AdapterFotos(private val lista: List<NasaGson>): RecyclerView.Adapter<AdapterFotos.ViewHolder>() {
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val binding = ActivityContadorBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.activity_contador, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.binding.txtTitulo.text = item.title.toString()
        Picasso.get().load(item.url).resize(150, 130).centerCrop().into(holder.binding.imagen)

    }

    override fun getItemCount(): Int {
        return lista.count()
    }
}