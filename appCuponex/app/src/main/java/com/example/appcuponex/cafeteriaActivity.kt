package com.example.appcuponex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appcuponex.poko.Promocion
import com.example.appcuponex.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

class cafeteriaActivity : AppCompatActivity() {
    var listaPromocion = ArrayList<Promocion>()
    var recyclerview: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CustomAdapter()
        //adapter.listaPromocion = listaPromocion



        setContentView(R.layout.activity_cafeteria)
        val cafeteria :Int   = 102
        mostrarCafeteria(cafeteria)
    }

    private fun mostrarCafeteria(cafeteria: Int){
        Ion.getDefault(this@cafeteriaActivity).conscryptMiddleware.enable(false)
        //implementacion de consumo
        Ion.with(this@cafeteriaActivity)
            .load("GET", Constantes.URL_WS+"promociones/obtenerPromocionCafeteria/${cafeteria}")
            .asString()
            .setCallback{e, result->
                if(e !=null){
                    mostrarAlerta(e.message!!)
                }else{
                    convertirJson(result)
                }
            }

    }

    private fun convertirJson(respuestaJson:String){
        val gson = Gson()
        val typePromocion = object : TypeToken<ArrayList<Promocion>>(){}.type
        listaPromocion = gson.fromJson(respuestaJson, typePromocion)
        cargarPromocion()
    }

    private fun cargarPromocion() {
        mostrarAlerta(listaPromocion.toString())
        val adaptador = CustomAdapter()
        adaptador.context = this@cafeteriaActivity
        adaptador.listaPromocion = listaPromocion
        recyclerview?.layoutManager = LinearLayoutManager(this)
        recyclerview?.adapter = adaptador

    }
    private fun mostrarAlerta(mensaje: String){
        Toast.makeText(this@cafeteriaActivity, mensaje, Toast.LENGTH_LONG).show()
    }


}