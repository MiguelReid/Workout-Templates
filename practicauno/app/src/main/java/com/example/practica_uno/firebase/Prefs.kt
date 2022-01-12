package com.example.practica_uno.firebase

import android.content.Context

class Prefs(val c: Context) {
    val FICHERO = "ficheroDatos"
    val EMAIL = "email"
    val PROVIDER = "provider"
    val storage = c.getSharedPreferences(FICHERO, 0)
    //siempre igual inicializar con el context y el 0 es en modo privado

    fun guardarEmail(email: String) {
        storage.edit().putString(EMAIL, email).apply()
    }

    fun guardarProvider(p: String) {
        storage.edit().putString(PROVIDER, p).apply()
    }

    fun leerEmail(): String? {
        return storage.getString(EMAIL, null)
    }

    fun leerProvider(): String? {
        return storage.getString(PROVIDER, null)
    }

    fun borrarTodo() {
        storage.edit().clear().apply()
    }
}