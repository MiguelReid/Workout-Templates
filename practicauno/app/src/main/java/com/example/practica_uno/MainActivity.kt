package com.example.practica_uno

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica_uno.database.Crud
import com.example.practica_uno.databinding.ActivityMainBinding
import com.example.practica_uno.databinding.CardviewlayoutBinding
import com.example.practica_uno.recyclerView.AdapterDatos
import com.example.practica_uno.recyclerView.Datos
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var lista: MutableList<Datos>
    val arrayList = ArrayList<Datos>()
    lateinit var adapter: AdapterDatos
    lateinit var bindingAux: CardviewlayoutBinding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingAux = CardviewlayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recycler()
        listeners()
        swipe()
        colores()

        val editor = getSharedPreferences("prefs", MODE_PRIVATE)
        val opcion = editor.getInt("OPCION", 0)

        if (opcion == 0) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        }
        if (opcion == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        }
        if (opcion == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            delegate.applyDayNight()
        }
        arrayList.addAll(lista)
    }

    private fun colores() {
        val colors = resources.getIntArray(R.array.colors)
        val aleatorio = (0..4).random()
        bindingAux.cardview.strokeColor = colors[aleatorio]
    }

    private fun recycler() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        fillAll()
        adapter = AdapterDatos(lista)
        binding.recyclerView.adapter = adapter
    }

    private fun fillAll() {
        val admin = Crud()
        lista = admin.select()
    }

    private fun swipe() {
        val touchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as AdapterDatos
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val ith = ItemTouchHelper(touchHelper)
        ith.attachToRecyclerView(binding.recyclerView)
    }

    private fun listeners() {
        binding.floatingActionButton2.setOnClickListener {
            val i = Intent(this, NuevoTemplate::class.java)
            startActivity(i)
        }
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    if (p0.isNotEmpty()) {
                        arrayList.clear()
                        val search = p0!!.lowercase(Locale.getDefault())
                        lista.forEach {
                            if (it.nombre.toLowerCase(Locale.getDefault()).contains(search)) {
                                arrayList.add(it)
                                adapter = AdapterDatos(arrayList)
                                binding.recyclerView.adapter = adapter
                            }
                        }
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                        return true
                    } else {
                        arrayList.clear()
                        //arrayList.addAll(lista)
                        fillAll()
                        adapter = AdapterDatos(lista)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
                return true
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        recycler()
    }

    override fun onResume() {
        super.onResume()
        recycler()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_opciones, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_tema -> {
                theme()
            }
            R.id.item_maps -> {
                val i3 = Intent(this, Mapas::class.java)
                startActivity(i3)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun theme() {

        val editor = getSharedPreferences("prefs", MODE_PRIVATE)
        val opcion = editor.getInt("OPCION", 0)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.theme))
        val temas = arrayOf("Light", "Dark", "System default")

        builder.setSingleChoiceItems(temas, opcion) { dialog, which ->

            when (which) {

                0 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                    val editor = getSharedPreferences("prefs", MODE_PRIVATE).edit()
                    editor.apply {
                        putInt("OPCION", 0)
                    }.apply()

                    dialog.dismiss()
                }
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()

                    val editor = getSharedPreferences("prefs", MODE_PRIVATE).edit()
                    editor.apply {
                        putInt("OPCION", 1)
                    }.apply()

                    dialog.dismiss()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    delegate.applyDayNight()

                    val editor = getSharedPreferences("prefs", MODE_PRIVATE).edit()
                    editor.apply {
                        putInt("OPCION", 2)
                    }.apply()

                    dialog.dismiss()
                }
                else -> {}
            }
        }

        val dialog = builder.create()
        dialog.show()
    }
}