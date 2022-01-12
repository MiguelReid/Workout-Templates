package com.example.practica_uno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practica_uno.databinding.ActivityMapasBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class Mapas : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityMapasBinding
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragment()
    }

    private fun fragment() {
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        marker()
    }

    private fun marker() {
        val coordenadas = LatLng(36.840018910566315, -2.470153153712873)
        val marker = MarkerOptions().position(coordenadas).title("Oficinas")
    }
}