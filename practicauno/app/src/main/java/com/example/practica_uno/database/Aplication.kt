package com.example.practica_uno.database

import android.app.Application
import android.content.Context

class Aplication: Application(){
    companion object{
        val BASE = "baseMeditar"
        val TABLA = "tablaMeditar"
        val VERSION = 1
        lateinit var appContext: Context
        lateinit var DB:DatabaseHelper
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        DB = DatabaseHelper()
    }
}