package com.example.appcuponex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.appcuponex.poko.RespuestaLogin
import com.example.appcuponex.poko.Usuario
import com.example.appcuponex.util.Constantes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.koushikdutta.ion.Ion

private lateinit var etNombre : EditText
private lateinit var etApellidoPaterno : EditText
private lateinit var etApellidoMaterno : EditText
private lateinit var etCorreo : TextView
private lateinit var etTelefono: EditText

private lateinit var etCalle: EditText
private lateinit var etNumero: EditText
private lateinit var etFecha: EditText
private lateinit var etPassword: EditText



class actualizarPerfilActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_perfil)
        val bundle: Bundle? = intent.extras
         val dato  = bundle?.getString("correo")

        etNombre = findViewById(R.id.etNombreActualizar)
        etApellidoPaterno = findViewById(R.id.etApellidoPaternoActualizar)
        etApellidoMaterno = findViewById(R.id.etApellidoMaternoActualizar)
        etTelefono = findViewById(R.id.etTelefonoActualizar)
        etCorreo = findViewById(R.id.tvCorreoValido)
        etCalle = findViewById(R.id.etCalleActualizar)
        etNumero = findViewById(R.id.etNumeroActualizar)
        etFecha = findViewById<EditText>(R.id.etFechaNacimientoActualizar)
        etPassword = findViewById(R.id.etPasswordActualizar)

        descargarInformacionUsuario(dato)
        val btnAceptar = findViewById<Button>(R.id.btnAceptarActualizar)
        btnAceptar.setOnClickListener {
            validarCampos()
        }
        etFecha.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun validarCampos(){
        val nombreTxt = etNombre.text.toString()
        val apellidoPaternoTxt = etApellidoPaterno.text.toString()
        val apellidoMaternoTxt = etApellidoMaterno.text.toString()
        val telefonoTxt = etTelefono.text.toString()
        val calleTxt = etCalle.text.toString()
        val correoTxt = etCorreo.text.toString()
        val numeroTxt = etNumero.text.toString()
        val fechaTxt = etFecha.text.toString()
        val passwordTxt = etPassword.text.toString()
        var valido = true
        if(nombreTxt.isEmpty()){
            valido = false
            etNombre.setError("Campo nombre requerido")
        }
        if(apellidoPaternoTxt.isEmpty()){
            valido = false
            etApellidoPaterno.setError("Campo apellido paterno requerido")
        }
        if(apellidoMaternoTxt.isEmpty()){
            valido = false
            etApellidoMaterno.setError("Campo apellido materno requerido")
        }
        if(telefonoTxt.isEmpty()){
            valido = false
            etTelefono.setError("Campo telefono  requerido")
        }
        if(calleTxt.isEmpty()){
            valido = false
            etCalle.setError("Campo telefono  requerido")
        }
        if(numeroTxt.isEmpty()){
            valido = false
            etNumero.setError("Campo telefono  requerido")
        }
        if(fechaTxt.isEmpty()){
            valido = false
            etFecha.setError("Campo telefono  requerido")
        }
        if(passwordTxt.isEmpty()){
            valido = false
            etPassword.setError("Campo contraseña requerido")
        }
        if(valido){
            enviarCredencialesActualizadas(nombreTxt, apellidoPaternoTxt, apellidoMaternoTxt, telefonoTxt, correoTxt,  calleTxt, numeroTxt, fechaTxt, passwordTxt)
        }
    }

    private fun enviarCredencialesActualizadas(nombre :String , apellidoPaterno:String, apellidoMaterno:String, telefono :String, correo:String, calle :String, numero:String,  fecha:String, password:String){

        Ion.getDefault(this@actualizarPerfilActivity).conscryptMiddleware.enable(false)
        //implementacion de consumo
        Ion.with(this@actualizarPerfilActivity)
            .load("PUT", Constantes.URL_WS+"usuarios/modificarUsuario")
            .setHeader("Content-Type","application/x-www-form-urlencoded")
            .setBodyParameter("nombre",nombre)
            .setBodyParameter("apellidoPaterno",apellidoPaterno)
            .setBodyParameter("apellidoMaterno",apellidoMaterno)
            .setBodyParameter("telefono",telefono)
            .setBodyParameter("correo",correo)
            .setBodyParameter("calle",calle)
            .setBodyParameter("numero",numero)
            .setBodyParameter("fechaNacimiento",fecha)
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
                val respuestaPeticion = respuestaWS.mensaje
                mostrarAlerta(respuestaPeticion)
                val irHome = Intent(this@actualizarPerfilActivity, homeActivity::class.java)
                startActivity(irHome)
                finish()
            }else{
                mostrarAlerta(respuestaWS.mensaje)
            }
        }
    }



    private fun descargarInformacionUsuario(correo :String?){
        Ion.with(this@actualizarPerfilActivity)
            .load("GET", Constantes.URL_WS+"usuarios/obtenerUsuarioPorCorreo/${correo}")
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
        val informacionUsuario = gson.fromJson(respuestaJson, Usuario::class.java)
        etNombre.setText(informacionUsuario.nombre)
        etApellidoPaterno.setText(informacionUsuario.apellidoPaterno)
        etApellidoMaterno.setText(informacionUsuario.apellidoMaterno)
        etTelefono.setText(informacionUsuario.telefono)
        etCorreo.text = informacionUsuario.correo
        etCalle.setText(informacionUsuario.calle)
        etNumero.setText(informacionUsuario.numero)
        etFecha.setText(informacionUsuario.fechaNacimiento)
        etPassword.setText(informacionUsuario.password)
    }

    private fun mostrarAlerta(mensaje: String){
        Toast.makeText(this@actualizarPerfilActivity, mensaje, Toast.LENGTH_LONG).show()
    }

    private fun  showDatePickerDialog(){
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        etFecha.setText("$year-$month-$day")
    }
}