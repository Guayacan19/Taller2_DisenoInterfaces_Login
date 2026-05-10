package com.example.taller2_fabian.iu.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.taller2_fabian.R
import com.example.taller2_fabian.SupabaseClient
import com.example.taller2_fabian.data.UsuarioRepository
import com.example.taller2_fabian.iu.autch.LoginActivity
import com.example.taller2_fabian.iu.main.admin.AdminFragment
import com.example.taller2_fabian.iu.main.admin.UsuariosFragment
import com.example.taller2_fabian.iu.main.perfil.PerfilFragment
import com.example.taller2_fabian.iu.main.productos.Carritoragment
import com.example.taller2_fabian.iu.main.productos.CatalogoFragment
import com.example.taller2_fabian.iu.main.productos.FavoritosFragment
import com.example.taller2_fabian.iu.main.productos.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        drawerLayout = findViewById(R.id.drawer_layout)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        val navView = findViewById<NavigationView>(R.id.nav_view)

        // CONFIGURAR MENU POR ROLcar
        configurarMenuPorRol(navView.menu)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        cargarFragmnet(HomeFragment())
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_home -> cargarFragmnet(HomeFragment())

                R.id.nav_catalogo -> cargarFragmnet(CatalogoFragment())

                R.id.nav_carrito -> cargarFragmnet(Carritoragment())

                R.id.nav_perfil -> cargarFragmnet(PerfilFragment())

                R.id.nav_favoritos -> cargarFragmnet(FavoritosFragment())
            }
            true
        }

        navView.setNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.nav_home -> {
                    cargarFragmnet(HomeFragment())
                    bottomNav.selectedItemId = R.id.nav_home
                }

                R.id.nav_catalogo -> {
                    cargarFragmnet(CatalogoFragment())
                    bottomNav.selectedItemId = R.id.nav_catalogo
                }

                R.id.nav_favoritos -> {
                    cargarFragmnet(FavoritosFragment())
                    bottomNav.selectedItemId = R.id.nav_favoritos
                }

                R.id.nav_carrito -> {
                    cargarFragmnet(Carritoragment())
                    bottomNav.selectedItemId = R.id.nav_carrito
                }

                R.id.nav_perfil -> {
                    cargarFragmnet(PerfilFragment())
                    bottomNav.selectedItemId = R.id.nav_perfil
                }

                R.id.nav_admin -> {
                    cargarFragmnet(AdminFragment())
                }

                R.id.nav_usuarios -> {
                    cargarFragmnet(UsuariosFragment())
                }

                R.id.nav_logout -> {
                    cerrarSesion()
                }
            }

            drawerLayout.closeDrawers()
            true
        }
    }

    private fun configurarMenuPorRol(menu: Menu) {

        lifecycleScope.launch {

            val rol = UsuarioRepository.obtenerRolActual()

            android.util.Log.d(
                "DEBUG_ROL",
                "Rol obtenido: $rol"
            )

            runOnUiThread {

                when (rol) {

                    "admin" -> {

                        // ADMIN VE TODO
                        menu.findItem(R.id.nav_admin).isVisible = true
                        menu.findItem(R.id.nav_usuarios).isVisible = true
                    }

                    "vendedor" -> {

                        // VENDEDOR
                        menu.findItem(R.id.nav_admin).isVisible = true
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                    }

                    else -> {

                        // COMPRADOR
                        menu.findItem(R.id.nav_admin).isVisible = false
                        menu.findItem(R.id.nav_usuarios).isVisible = false
                    }
                }
            }
        }
    }

    private fun cargarFragmnet(fragment: Fragment) {

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun cerrarSesion() {

        lifecycleScope.launch {

            try {

                SupabaseClient.client.auth.signOut()

                runOnUiThread {

                    startActivity(
                        Intent(
                            this@MainActivity,
                            LoginActivity::class.java
                        )
                    )

                    finishAffinity()
                }

            } catch (e: Exception) {

                runOnUiThread {

                    Toast.makeText(
                        this@MainActivity,
                        "Error al cerrar sesión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}