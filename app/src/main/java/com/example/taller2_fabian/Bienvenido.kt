package com.example.taller2_fabian

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Bienvenido : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)

        // BOTON COMIENZA
        val boton = findViewById<Button>(R.id.buttonComienza)

        boton.setOnClickListener {
            val intent = Intent(this, loguin::class.java)
            startActivity(intent)
        }

        // TEXTO REGISTRATE
        val registrate = findViewById<TextView>(R.id.textViewRegistrate)

        registrate.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }
}