package com.example.taller2_fabian.iu.autch

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.taller2_fabian.R
import com.example.taller2_fabian.iu.autch.RegistroActivity
import com.example.taller2_fabian.iu.main.MainActivity
import android.widget.Button
import android.widget.LinearLayout

private lateinit var tvRegistrate: TextView
private lateinit var tvRecuperarContrasena: TextView
private lateinit var tvIngresarConGoogle: LinearLayout
private lateinit var tvIngresarConHuella: TextView

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loguin)

        tvIngresarConHuella = findViewById(R.id.in_huella)

        // Inicio de sesion con huella
        TvIngresarConGoogle.setOnclickListener {

            mostrarDialogHuella()



        }



        val rootView = findViewById<ScrollView>(id = R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )
            insets
        }

        etCorreo = findViewById(R.id.in_correo)
        etContrasena = findViewById(R.id.in_contrasena)
        btnInicioSesion = findViewById(R.id.btn_inicio_sesion)
        tvRegistrate = findViewById(R.id.in_tv_registrate)
        tvRecuperarContrasena = findViewById(R.id.in_recuperar_contrasena)
        tvIngresarConGoogle = findViewById(R.id.btn_google)

        // TEXTO REGISTRATE
        val registrate = findViewById<TextView>(R.id.txtRegistrate)

        registrate.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)

        }

        // Inicio de sesión con información de Supabase
        btnInicioSesion.setOnClickListener {
            inicarSesion()
        }

// Inicio de sesión con google
        tvIngresarConGoogle.setOnClickListener {
            iniciarSesionConGoogle()
        }

            btnContinuar.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

    private fun inicarSesion(){}

    private  fun iniciarSesionConGoogle(){}

    private fun mostrarDialogHuella() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                val correo = CredencialesManager.obtenerCorreo(this@LoginActivity)
                val contrasena = CredencialesManager.obtenerContrasena(this@LoginActivity)

                if (correo != null && contrasena != null) {
                    lifecycleScope.launch {
                        try {
                            SupabaseClient.client.auth.signInWith(provider = Email) {
                                email = correo
                                password = contrasena
                            }
                            startActivity(Intent(packageContext = this@LoginActivity, cls = MainActivity::class.java))
                            finishAffinity()


                        }
                    }
                } else {
                    // No hay credencial - No logueado previamente - limpiar-ocultarla
                    Toast.makeText(this@LoginActivity, "Inicia sesion con tu email", Toast.LENGTH_SHORT).show()
                    CredencialesManager.limpiarCredenciales(this@LoginActivity)
                }


                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if(errorCode != BiometricPrompt.ERROR_USER_CANCELED &&
                        errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                        Toast.makeText(context = this@LoginActivity, text = "Error biometrice: $errString", duration = Toast.LEN
                    }
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(context = this@LoginActivity, text = "Autenticación fallida", duration = Toast.LENGTH_SHORT).show()
                }

                }




            }
        })
    }
    }
