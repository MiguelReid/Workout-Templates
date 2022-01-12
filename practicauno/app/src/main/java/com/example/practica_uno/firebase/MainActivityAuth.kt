package com.example.practica_uno.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_uno.MainActivity
import com.example.practica_uno.R
import com.example.practica_uno.databinding.ActivityMainAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

enum class Providers {
    BASIC,
    GOOGLE
}

class MainActivityAuth : AppCompatActivity() {

    lateinit var binding: ActivityMainAuthBinding
    var email = ""
    var pass = ""
    lateinit var prefs: Prefs

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

            if (it.resultCode == RESULT_OK) {

                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    //Aqui tengo los datos de la cuenta
                    val cuenta = task.getResult(ApiException::class.java)
                    if (cuenta != null) {
                        val credenciales = GoogleAuthProvider.getCredential(cuenta.idToken, null)
                        FirebaseAuth.getInstance().signInWithCredential(credenciales)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    irApp(cuenta.email ?: "", Providers.GOOGLE)
                                } else {
                                    mostrarError()
                                }
                            }
                    }
                } catch (e: ApiException) {
                    Log.d("ERROR CON API>>>>>>>>", e.message.toString())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMainAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        setUp()
        comprobarSesion()
    }

    private fun comprobarSesion() {
        val e = prefs.leerEmail()
        val p = prefs.leerProvider()
        if (!e.isNullOrEmpty() && !p.isNullOrEmpty()) {
            irApp(e, Providers.valueOf(p))
        }
    }

    private fun setUp() {
        title = "Autenticacion"
        binding.btnRegistro.setOnClickListener {
            registrar()
        }
        binding.btnLogin.setOnClickListener {
            acceder()
        }
        binding.btnSignin.setOnClickListener {
            accederConGoogle()
        }
    }

    private fun accederConGoogle() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("795198305784-639sbro81sjojt8a4bhh2ajft5dchabm.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, gso)
        googleClient.signOut()
        responseLauncher.launch(googleClient.signInIntent)

    }

    private fun acceder() {
        //if return igual que un if else pero mas rapido y comodo
        if (!cogerDatos()) return
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                //el registro ha ido bien
                irApp(it.result?.user?.email ?: "", Providers.BASIC)
                //el ?:"" es por si no encuentra nada q devuelva ""
            } else {
                Log.d("ERROR------->", it.exception.toString())
                mostrarError()
            }
        }

    }

    private fun cogerDatos(): Boolean {
        email = binding.etEmail.text.toString().trim()
        pass = binding.etPass.text.toString().trim()
        if (email.isEmpty()) {
            binding.etEmail.error = "Rellene el campo email!!"
            return false
        }
        if (pass.isEmpty()) {
            binding.etPass.error = "Rellene el campo contraseña!!"
            return false
        }
        return true
    }

    private fun registrar() {


        if (!cogerDatos()) return
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //el registro ha ido bien
                    irApp(it.result?.user?.email ?: "", Providers.BASIC)
                    //el ?:"" es por si no encuentra nada q devuelva ""
                } else {
                    Log.d("ERROR------->", it.exception.toString())
                    mostrarError()
                }
            }
    }

    private fun irApp(email: String?, provider: Providers) {
        //clase prviders es un enum d kotlin
        limpiar()
        val i = Intent(this, MainActivity::class.java).apply {
            putExtra("EMAIL", email)
            putExtra("PROVIDER", provider.name)
        }
        startActivity(i)
    }

    private fun limpiar() {
        binding.etPass.setText("")
        binding.etEmail.setText("")
        binding.etEmail.requestFocus()
    }

    private fun mostrarError() {
        val alerta = AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Se ha producido un error")
            .setPositiveButton("Aceptar", null)
            .create()
            .show()
    }
}