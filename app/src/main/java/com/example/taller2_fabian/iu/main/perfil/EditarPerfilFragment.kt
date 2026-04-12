package com.example.taller2_fabian.iu.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.taller2_fabian.R

class EditarPerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editar_perfil, container, false)

        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar)

        btnGuardar.setOnClickListener {

        }

        return view
    }
}