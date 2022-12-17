package com.example.appcuponex


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcuponex.poko.RespuestaLogin
import com.example.appcuponex.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion

class MainActivity : AppCompatActivity() {
    private lateinit var etUsuario: EditText
    private lateinit var etContrasenia : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Iniciar Sesion"
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        etUsuario = findViewById(R.id.etUsuario)
        etContrasenia = findViewById(R.id.etPassword)
        btnIniciarSesion.setOnClickListener {
            validacionCampos()
        }
        btnRegistrarse.setOnClickListener {
            irRegistro()
        }

    }

    private fun validacionCampos(){
         val textoCorreo =etUsuario.text.toString()
         val textoContrasena = etContrasenia.text.toString()
         var validacion =true
        if (textoCorreo.isEmpty()){
            validacion = false
            etUsuario.setError("Por favor llena el campo de correo")
        }
        if(textoContrasena.isEmpty()){
            validacion = false
            etContrasenia.setError("Por favor llena el campo de contraseña")
        }
        if(validacion){
            enviarCredenciales(textoCorreo, textoContrasena)
        }
    }

    private fun enviarCredenciales(correo : String, password: String) {
        Ion.getDefault(this@MainActivity).conscryptMiddleware.enable(false)
        Ion.with(this@MainActivity)
            .load("POST",Constantes.URL_WS+"acceso/movil")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("correo",correo)
            .setBodyParameter("password",password)
            .asString()
            .setCallback { e, result ->
                if(e!=null){
                    //hubo error
                    e.printStackTrace()
                    mostrarAlerta("Error en la conexion, intente mas tarde")
                }else{
                    //ok - 200
                    validarResultadoPeticion(result)
                }
            }

    }
    private fun validarResultadoPeticion(respuesta: String){
        if(respuesta == null || respuesta.isEmpty()){
            mostrarAlerta("Por el momento el servicio no está disponible")
        }else{
            val gson = Gson()
            val respuestaWS = gson.fromJson(respuesta, RespuestaLogin::class.java)
            if(!respuestaWS.error!!){
                val nombreCompleto = respuestaWS.mensaje
                mostrarAlerta(nombreCompleto)
                val irVentanaInicio = Intent(this@MainActivity, homeActivity::class.java)
                etUsuario = findViewById<EditText>(R.id.etUsuario)
                etContrasenia = findViewById(R.id.etPassword)
                irVentanaInicio.putExtra("correo", etUsuario.text.toString())
                startActivity(irVentanaInicio)
                finish()
            }else{
                mostrarAlerta(respuestaWS.mensaje)
            }
        }
    }

    private fun mostrarAlerta(mensaje: String){
        Toast.makeText(this@MainActivity, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun irRegistro(){
        val irVentanaRegistro = Intent(this@MainActivity, RegistroActivity::class.java)
        startActivity(irVentanaRegistro)
        finish()
    }
}