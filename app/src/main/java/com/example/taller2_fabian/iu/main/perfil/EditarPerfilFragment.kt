package com.example.taller2_fabian.iu.main.perfil

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.taller2_fabian.R
import com.example.taller2_fabian.SupabaseClient
import com.example.taller2_fabian.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import java.io.File

class EditarPerfilFragment : Fragment() {

    private var uriFotoSeleccionada: Uri? = null

    private lateinit var ivEditarFoto: ImageView
    private lateinit var archivoFotoTemp: File

    // permiso cámara
    private val lanzadorPermisoCamara =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { concedido ->

            if (concedido) {

                abrirCamara()

            } else {

                Toast.makeText(
                    requireContext(),
                    "Permiso de cámara denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    // cámara
    private val lanzadorCamara =
        registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { exito ->

            if (exito) {

                uriFotoSeleccionada = Uri.fromFile(archivoFotoTemp)

                ivEditarFoto.load(uriFotoSeleccionada) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    // galería
    private val lanzadorGaleria =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                uriFotoSeleccionada = uri

                ivEditarFoto.load(uri) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_editar_perfil,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivEditarFoto = view.findViewById(R.id.iv_editar_foto)

        val ivCamaraIcon =
            view.findViewById<ImageView>(R.id.iv_camara_icon)

        val etNombres =
            view.findViewById<EditText>(R.id.et_nombres)

        val etApellidos =
            view.findViewById<EditText>(R.id.et_apellidos)

        val etCorreo =
            view.findViewById<EditText>(R.id.et_correo)

        val etContrasena =
            view.findViewById<EditText>(R.id.et_password)

        val etReContrasena =
            view.findViewById<EditText>(R.id.et_repassword)

        val btnGuardar =
            view.findViewById<Button>(R.id.btn_guardar)

        // cargar datos actuales
        lifecycleScope.launch {

            val usuario = UsuarioRepository.obtenerUsuarioActual()

            if (usuario != null) {

                etNombres.setText(usuario.nombres)
                etApellidos.setText(usuario.apellidos)
                etCorreo.setText(usuario.correo ?: "")

                if (!usuario.foto_url.isNullOrEmpty()) {

                    ivEditarFoto.load(usuario.foto_url) {

                        transformations(CircleCropTransformation())

                        placeholder(R.mipmap.ic_launcher_round)
                        error(R.mipmap.ic_launcher_round)
                    }
                }
            }
        }

        // cámara o galería
        ivCamaraIcon.setOnClickListener {

            mostrarOpcionesFoto()
        }

        // guardar cambios
        btnGuardar.setOnClickListener {

            guardarCambios(
                etNombres,
                etApellidos,
                etCorreo,
                etContrasena,
                etReContrasena
            )
        }
    }

    private fun mostrarOpcionesFoto() {

        val opciones = arrayOf(
            "Tomar foto",
            "Elegir de galería"
        )

        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Foto de perfil")
            .setItems(opciones) { _, cual ->

                when (cual) {

                    0 -> verificarPermisoCamara()

                    1 -> lanzadorGaleria.launch("image/*")
                }
            }
            .show()
    }

    private fun verificarPermisoCamara() {

        when {

            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

                abrirCamara()
            }

            else -> {

                lanzadorPermisoCamara.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private fun abrirCamara() {

        val carpeta =
            File(requireContext().cacheDir, "images")

        carpeta.mkdirs()

        archivoFotoTemp =
            File(carpeta, "foto_perfil_temp.jpg")

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            archivoFotoTemp
        )

        lanzadorCamara.launch(uri)
    }

    private fun guardarCambios(
        etNombres: EditText,
        etApellidos: EditText,
        etCorreo: EditText,
        etContrasena: EditText,
        etReContrasena: EditText
    ) {

        val nombres =
            etNombres.text.toString().trim()

        val apellidos =
            etApellidos.text.toString().trim()

        val correo =
            etCorreo.text.toString().trim()

        val contrasena =
            etContrasena.text.toString()

        val reContrasena =
            etReContrasena.text.toString()

        // validaciones
        if (
            nombres.isEmpty() ||
            apellidos.isEmpty() ||
            correo.isEmpty()
        ) {

            Toast.makeText(
                requireContext(),
                "Complete todos los campos",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (contrasena.isNotEmpty()) {

            if (contrasena.length < 6) {

                Toast.makeText(
                    requireContext(),
                    "La contraseña debe tener mínimo 6 caracteres",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            if (contrasena != reContrasena) {

                Toast.makeText(
                    requireContext(),
                    "Las contraseñas no coinciden",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }
        }

        lifecycleScope.launch {

            try {

                // subir foto
                var fotoUrl: String? = null

                if (uriFotoSeleccionada != null) {

                    fotoUrl =
                        UsuarioRepository.subirFotoPerfil(
                            requireContext(),
                            uriFotoSeleccionada!!
                        )
                }

                // actualizar perfil
                UsuarioRepository.actualizarPerfil(
                    nombres = nombres,
                    apellidos = apellidos,
                    correo = correo,
                    fotoUrl = fotoUrl
                )

                // actualizar contraseña
                if (contrasena.isNotEmpty()) {

                    SupabaseClient.client.auth.updateUser {

                        password = contrasena
                    }
                }

                runOnUiThread {

                    Toast.makeText(
                        requireContext(),
                        "Perfil actualizado",
                        Toast.LENGTH_SHORT
                    ).show()

                    parentFragmentManager.popBackStack()
                }

            } catch (e: Exception) {

                runOnUiThread {

                    Toast.makeText(
                        requireContext(),
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun runOnUiThread(action: () -> Unit) {

        activity?.runOnUiThread(action)
    }
}