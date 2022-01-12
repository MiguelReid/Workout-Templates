package com.example.practica_uno.database

import android.database.DatabaseUtils
import android.util.Log
import com.example.practica_uno.database.Aplication.Companion.DB
import com.example.practica_uno.recyclerView.Datos
import java.sql.Date
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class Crud {

    lateinit var fechaFinal: Date

    fun insert(reserva: Datos) {
        val insert =
            "INSERT INTO ${Aplication.TABLA}(nombre, tiempo, creacion)" + "values('${reserva.nombre}','${reserva.tiempo}','${reserva.creacion}');"
        val conexion = DB.writableDatabase
        try {
            conexion.execSQL(insert)
            conexion.close()
        } catch (ex: Exception) {
            Log.d("ERROR DEL INSERT->>>>", ex.message.toString())
        }
    }

    fun select(): MutableList<Datos> {
        val lista = mutableListOf<Datos>()
        val consulta = "SELECT * FROM ${Aplication.TABLA} ORDER BY nombre"
        val conexion = DB.readableDatabase
        val total = DatabaseUtils.queryNumEntries(conexion, Aplication.TABLA)

        if (total > 0) {
            try {
                val element = conexion.rawQuery(consulta, null)
                while (element.moveToNext()) {
                    val fecha: String = element.getString(3)

                    val date = Date.valueOf(fecha) //converting string into sql date

                    //val iso8601Format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    //HE PROBADO A QUITAR HH:mm:ss no se si funcionara luego

                    var item = Datos(
                        element.getInt(0),
                        element.getString(1),
                        element.getLong(2),
                        date,
                    )
                    lista.add(item)
                }
            } catch (ex: Exception) {
                Log.d("ERROR SELECT->>>>", ex.message.toString())
            }
        }
        return lista
    }

    fun delete(id: Int) {
        val delete = "DELETE FROM ${Aplication.TABLA} WHERE id = $id"
        val conexion = DB.writableDatabase
        try {
            conexion.execSQL(delete)
            conexion.close()
        } catch (ex: Exception) {
            Log.d("ERROR DEL DELETE->>>>", ex.message.toString())
        }
    }

    fun update(obj: Datos) {
        val delete =
            "UPDATE ${Aplication.TABLA} SET nombre='${obj.nombre}', tiempo = '${obj.tiempo}', creacion = '${obj.creacion}' WHERE id = ${obj.id}"
        val conexion = DB.writableDatabase
        try {
            conexion.execSQL(delete)
            conexion.close()
        } catch (ex: Exception) {
            Log.d("ERROR DEL UPDATE->>>>", ex.message.toString())
        }
    }

    fun drop() {
        val delete = "DELETE FROM ${Aplication.TABLA}"
        val conexion = DB.writableDatabase
        try {
            conexion.execSQL(delete)
            conexion.close()
        } catch (ex: Exception) {
            Log.d("ERROR DEL DROP->>>>", ex.message.toString())
        }
    }
}