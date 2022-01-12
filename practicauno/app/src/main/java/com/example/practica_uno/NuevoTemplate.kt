package com.example.practica_uno

import android.R.attr
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_uno.databinding.ActivityNuevotemplateBinding
import com.example.practica_uno.recyclerView.Datos
import java.sql.Date
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.LocalDate
import java.time.LocalTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import android.R.attr.x
import android.util.Log
import android.widget.Toast
import com.example.practica_uno.database.Crud
import java.util.*


class NuevoTemplate : AppCompatActivity() {

    lateinit var binding: ActivityNuevotemplateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevotemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listener()
    }

    private fun listener() {
        binding.btnReset.setOnClickListener {
            binding.txtNewDuracion.setText("")
            binding.txtNewName.setText("")
        }
        binding.btnSave.setOnClickListener {
            save()
        }
    }

    private fun save() {
        val nombre = binding.txtNewName.text.toString().trim()
        val duracion = binding.txtNewDuracion.text.toString().trim()
        if (nombre.isEmpty()) {
            binding.txtNewName.error = "INTRODUCE UN NOMBRE"
        }
        if (duracion.isEmpty()) {
            binding.txtNewDuracion.error = "INTRODUCE LA DURACION"
        }

        val date = Date(Calendar.getInstance().time.time)
        //Para que automaticamente se ponga la fecha de hoy

        val aux = Datos(1,nombre,duracion.toLong(),date)
        val admin = Crud()
        admin.insert(aux)
        Toast.makeText(this,"Contacto Guardado", Toast.LENGTH_LONG).show()
        onBackPressed()
    }
}
