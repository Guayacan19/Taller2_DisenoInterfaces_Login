package com.example.taller2_fabian.iu.autch

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taller2_fabian.R
import kotlinx.serialization.Serializable

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistro: Button
    private lateinit var tvCuenta: TextView

    @Serializable
    data class UsuarioData(
        val id: String,
        val nombres: String,
        val apellidos: String
    )




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        // BOTON VOLVER
        val volver = findViewById<TextView>(R.id.textVolverLogin)

        volver.setOnClickListener {
            finish()
        }

        val rootView = findViewById<ViewGroup>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)

            insets


            etNombres = findViewById(R.id.re_nombres)
            etApellidos = findViewById(R.id.re_apellidos)
            etCorreo = findViewById(R.id.re_correo)
            etContrasena = findViewById(R.id.re_contrasena)
            etReContrasena = findViewById(R.id.re_recontrasena)
            checkTerminos = findViewById(R.id.check_terminos)
            btnRegistro = findViewById(R.id.btn_registro)
            tvCuenta = findViewById(R.id.re_cuenta)
        }
// Escuchar el botón de registro
        btnRegistro.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()
        }


    }
}