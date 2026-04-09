package com.example.taller2_fabian.iu.main.productos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taller2_fabian.R

class HomeFragment : Fragment() {

    private val listaProductos = listOf(
        Product(nombre = "Camisa Casual", precio = 29.9, imagenRes = R.drawable.camisa_casual),
        Product(nombre = "Camisa Sport", precio = 39.9, imagenRes = R.drawable.camisa_casual),
        Product(nombre = "Pantalón Jeans", precio = 49.9, imagenRes = R.drawable.camisa_casual),
        Product(nombre = "Zapatos Deportivos", precio = 59.9, imagenRes = R.drawable.camisa_casual)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos)

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = ProductoAdapter(listaProductos)

        return view
    }
}