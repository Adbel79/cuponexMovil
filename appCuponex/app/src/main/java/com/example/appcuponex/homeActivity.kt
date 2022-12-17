package com.example.appcuponex

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class homeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private   var correo : String? = null
    private lateinit var drawer :DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
         correo = bundle?.getString("correo")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navegacion_abierta, R.string.navegacion_cerrada)
        drawer.addDrawerListener(toggle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener ( this )

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_1 -> llamarEditarPerfil()
            R.id.nav_item_2 -> llamarAerolinea()
            R.id.nav_item_3 -> llamarCafeteria()
            R.id.nav_item_4 -> llamarFarmacia()

        }
        drawer.closeDrawer(GravityCompat.START)
        return  true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        if (toggle.onOptionsItemSelected(item)) {
        return true
        }
        return super.onOptionsItemSelected(item)
    }



    private fun llamarEditarPerfil(){
        val irVentanaPerfil = Intent(this@homeActivity, actualizarPerfilActivity::class.java)
       irVentanaPerfil.putExtra("correo", correo)
        startActivity(irVentanaPerfil)
        onPause()
    }
    private fun llamarCafeteria(){
        val irVentanaCafeteria = Intent(this@homeActivity, cafeteriaActivity::class.java)
        startActivity(irVentanaCafeteria)
        onPause()
    }
    private fun llamarAerolinea(){
        val irVentanaCafeteria = Intent(this@homeActivity, aerolineaActivity::class.java)
        startActivity(irVentanaCafeteria)
        onPause()
    }
    private fun llamarFarmacia(){
        val irVentanaFarmacia = Intent(this@homeActivity, farmaciaActivity::class.java)
        startActivity(irVentanaFarmacia)
        onPause()
    }
}
