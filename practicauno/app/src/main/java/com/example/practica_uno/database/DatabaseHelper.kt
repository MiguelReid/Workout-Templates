package com.example.practica_uno.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.practica_uno.database.Aplication.Companion.BASE
import com.example.practica_uno.database.Aplication.Companion.VERSION
import com.example.practica_uno.database.Aplication.Companion.appContext

class DatabaseHelper : SQLiteOpenHelper(appContext, BASE, null, VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        val crearTabla = "CREATE TABLE ${Aplication.TABLA}(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "tiempo MEDIUMTEXT NOT NULL, " +
                "creacion DATE NOT NULL);"
        //REVISAR SI ES MEDIUMTEXT DE VD
        Log.d("BASE DE DATOS->>>>", crearTabla)
        p0?.execSQL(crearTabla)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val drop = "DROP TABLE IF EXISTS ${Aplication.TABLA}"
        p0?.execSQL(drop)
        onCreate(p0)
    }
}