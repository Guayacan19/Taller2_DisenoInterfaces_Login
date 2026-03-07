package com.example.taller2_fabian

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class loguin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loguin)

        // TEXTO REGISTRATE
        val registrate = findViewById<TextView>(R.id.txtRegistrate)

        registrate.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }
}