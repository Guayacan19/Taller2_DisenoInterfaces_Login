package com.example.taller2_fabian.iu.inicio2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taller2_fabian.R
import com.example.taller2_fabian.iu.autch.LoginActivity
import com.example.taller2_fabian.iu.autch.RegistroActivity

class BienvenidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)

        // BOTON COMIENZA
        val boton = findViewById<Button>(R.id.buttonComienza)

        boton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // TEXTO REGISTRATE
        val registrate = findViewById<TextView>(R.id.textViewRegistrate)

        registrate.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}