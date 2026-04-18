package com.example.taller2_fabian.iu.autch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.taller2_fabian.R
import com.example.taller2_fabian.SupabaseClient
import com.example.taller2_fabian.data.CredencialesManagere
import com.example.taller2_fabian.iu.main.MainActivity
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText
    private lateinit var btnInicioSesion: Button
    private lateinit var tvRegistrate: TextView
    private lateinit var tvRecuperarContrasena: TextView
    private lateinit var btnGoogle: Button
    private lateinit var tvHuella: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loguin)


        etCorreo = findViewById(R.id.editUsuario)
        etContrasena = findViewById(R.id.editContrasena)
        btnInicioSesion = findViewById(R.id.btnIngresar)
        tvRegistrate = findViewById(R.id.txtRegistrate)
        tvRecuperarContrasena = findViewById(R.id.txtRecuperarContrasena)
        btnGoogle = findViewById(R.id.btnGoogle)
        tvHuella = findViewById(R.id.txtHuella)



        tvRegistrate.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }


        btnInicioSesion.setOnClickListener {
            iniciarSesion()
        }


        btnGoogle.setOnClickListener {
            iniciarSesionConGoogle()
        }


        tvHuella.setOnClickListener {
            mostrarDialogHuella()
        }
    }

    override fun onResume() {
        super.onResume()
        configurarVisibilidadHuella()
    }

    private fun configurarVisibilidadHuella() {
        val huellaActiva = CredencialesManagere.huellaActiva(this)

        val biometricManager = BiometricManager.from(this)
        val disponible = biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS

        tvHuella.visibility = if (huellaActiva && disponible) View.VISIBLE else View.GONE
    }

    private fun iniciarSesion() {
        val correo = etCorreo.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                SupabaseClient.client.auth.signInWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                    email = correo
                    password = contrasena
                }

                Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()

                CredencialesManagere.guardarCredenciales(this@LoginActivity, correo, contrasena, true)

            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun iniciarSesionConGoogle() {

    }

    private fun mostrarDialogHuella() {
        val executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {

                    val correo = CredencialesManagere.obtenerCorreo(this@LoginActivity)
                    val contrasena = CredencialesManagere.obtenerContrasena(this@LoginActivity)

                    if (correo != null && contrasena != null) {
                        lifecycleScope.launch {
                            try {
                                SupabaseClient.client.auth.signInWith(io.github.jan.supabase.auth.providers.builtin.Email) {
                                    email = correo
                                    password = contrasena
                                }

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finishAffinity()

                            } catch (e: Exception) {
                                Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onAuthenticationFailed() {
                    Toast.makeText(this@LoginActivity, "Huella incorrecta", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio con huella")
            .setSubtitle("Coloca tu dedo")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
