package com.example.taller2_fabian

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class loguin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loguin)

        val txt = findViewById<TextView>(R.id.txtRegistrate)
        txt.text = Html.fromHtml(getString(R.string.registro_texto), Html.FROM_HTML_MODE_COMPACT)


    }
}