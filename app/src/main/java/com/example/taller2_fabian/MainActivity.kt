package com.example.taller2_fabian

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, Bienvenido::class.java)
            startActivity(intent)
            finish()

        }, 2000) // 2000 milisegundos = 2 segundos
    }
}