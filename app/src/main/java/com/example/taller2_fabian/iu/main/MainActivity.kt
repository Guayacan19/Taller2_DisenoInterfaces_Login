package com.example.taller2_fabian.iu.main

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.taller2_fabian.R
import com.example.taller2_fabian.iu.main.admin.AdminFragment
import com.example.taller2_fabian.iu.main.admin.UsuariosFragment
import com.example.taller2_fabian.iu.main.perfil.PerfilFragment
import com.example.taller2_fabian.iu.main.productos.Carritoragment
import com.example.taller2_fabian.iu.main.productos.CatalogoFragment
import com.example.taller2_fabian.iu.main.productos.FavoritosFragment
import com.example.taller2_fabian.iu.main.productos.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

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
            }

            drawerLayout.closeDrawers()
            true
        }
    }


    private fun cargarFragmnet(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}