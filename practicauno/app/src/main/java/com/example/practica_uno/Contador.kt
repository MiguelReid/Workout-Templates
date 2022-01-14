package com.example.practica_uno

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_uno.databinding.ActivityContadorBinding
import com.example.practica_uno.nasa.ApiService
import com.example.practica_uno.nasa.NasaGson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Contador : AppCompatActivity() {

    val datosList = mutableListOf<NasaGson>()
    lateinit var binding: ActivityContadorBinding
    lateinit var audioManager: AudioManager
    lateinit var mp: MediaPlayer
    var tiempo: Long = 0
    private val URL_BASE = "https://api.nasa.gov/planetary/"
    private val KEY = "apod?api_key=YOUR_NASA_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContadorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cogerDatos()
        listener()
        inicializaMedia()
        volumen()

        var like = false

        binding.imgHeart.setOnClickListener {
            like = heart(R.raw.heart, like)
        }
    }

    private fun heart(animation: Int, like: Boolean): Boolean {
        if (!like) {
            binding.imgHeart.setAnimation(animation)
            binding.imgHeart.playAnimation()
        } else {
            binding.imagen.setImageResource(R.drawable.heartimg)
        }
        return !like
    }

    private fun imagen() {
        CoroutineScope(Dispatchers.Main).launch {
            //BIEN AL CAMBIAR EL .IO POR .MAIN
            val llamada = getRetrofit().create(ApiService::class.java).getDatosNasa("apod?api_key=ebK4WNzaTu7Neg0nZJLxNMwbVIsuzbcMgn4CZfiI")
            val datos = llamada.body()

            if (llamada.isSuccessful) {
                if (datos != null) {
                    //datosList.add(datos)
                    binding.txtTitulo.text = datos.title
                    Picasso.get()!!.load(datos.url).resize(150, 130).centerCrop().into(binding.imagen)
                }
            } else {
                Toast.makeText(this@Contador, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun inicializaMedia() {
        mp = MediaPlayer.create(this, R.raw.relax)
    }

    private fun volumen() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun listener() {
        binding.btnExit.setOnClickListener {
            onBackPressed()
        }
        binding.btnIniciar2.setOnClickListener {
            run()
        }
    }

    private fun run() {
        binding.switch1.isClickable = false
        //_ es un parametro anonimo
        if (binding.switch1.isChecked) {
            imagen()
            mp.isLooping = true
            if (!mp.isPlaying) mp.start()
            contador("respira", "relajate", "namaste")
            binding.imagen.visibility = View.VISIBLE

        } else {
            if (mp.isPlaying) mp.pause()
            contador("calienta", "a tope", "perfecto")
            binding.imagen.visibility = View.INVISIBLE
        }

    }

    private fun contador(msg1: String, msg2: String, msg3: String) {
        val aux: Long = binding.txtContador.text.toString().toLong()
        //Log.d("EL TIEMPO9--->>>>", aux2.toString())
        val tiempo = (aux * 1000 + 5000)
        binding.txtApoyo.text = msg1

        object : CountDownTimer(tiempo, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.txtContador.text = (millisUntilFinished / 1000).toString()
                Handler().postDelayed({
                    binding.txtApoyo.text = msg2
                }, 5000)
            }

            override fun onFinish() {
                binding.txtApoyo.text = msg3
            }
        }.start()
    }

    fun cogerDatos() {
        val bundle = intent.extras
        tiempo = bundle?.getLong("TIEMPO2", 0)!!
        //Log.d("EL TIEMPO->>>>>", tiempo.toString())
        binding.txtContador.text = tiempo.toString()
    }

    override fun onStop() {
        super.onStop()
        mp.release()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
