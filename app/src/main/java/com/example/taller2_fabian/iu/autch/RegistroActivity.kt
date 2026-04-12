package com.example.taller2_fabian.iu.autch

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.taller2_fabian.R
import com.example.taller2_fabian.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
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

        // Referencias
        etNombres = findViewById(R.id.editNombre)
        etApellidos = findViewById(R.id.editApellidos)
        etCorreo = findViewById(R.id.editCorreo)
        etContrasena = findViewById(R.id.editPassword)
        etReContrasena = findViewById(R.id.editRepetirPassword)
        checkTerminos = findViewById(R.id.checkTerminos)
        btnRegistro = findViewById(R.id.buttonRegistrar)
        tvCuenta = findViewById(R.id.textVolverLogin)

        // Volver
        tvCuenta.setOnClickListener {
            finish()
        }

        // Insets
        val rootView = findViewById<ViewGroup>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = maxOf(systemBars.bottom, imeInsets.bottom)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
            insets
        }

        // Botón registro
        btnRegistro.setOnClickListener {

            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val reContrasena = etReContrasena.text.toString().trim()

            // VALIDACIONES
            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || contrasena.isEmpty() || reContrasena.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena.length < 8) {
                Toast.makeText(this, "Mínimo 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != reContrasena) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkTerminos.isChecked) {
                Toast.makeText(this, "Debe aceptar los términos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //  //Registro en supabase
            lifecycleScope.launch {
                try {

                    // 1. Crear usuario Auth
                    SupabaseClient.client.auth.signUpWith(Email) {
                        email = correo
                        password = contrasena
                    }

                    // 2. Guardar datos en tabla
                    val userId = SupabaseClient.client.auth.currentUserOrNull()?.id ?: ""

                    SupabaseClient.client.postgrest["usuarios"].insert(
                        UsuarioData(
                            id = userId,
                            nombres = nombres,
                            apellidos = apellidos
                        )
                    )

                    // 3. Éxito
                    Toast.makeText(this@RegistroActivity, "Registro exitoso", Toast.LENGTH_LONG).show()

                    startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                    finish()

                } catch (e: Exception) {
                    Toast.makeText(this@RegistroActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}