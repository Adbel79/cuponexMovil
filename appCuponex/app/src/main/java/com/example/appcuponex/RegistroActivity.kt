package com.example.appcuponex


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.appcuponex.poko.RespuestaLogin
import com.example.appcuponex.util.Constantes
import com.google.gson.Gson
import com.koushikdutta.ion.Ion


private lateinit var etNombre : EditText
private lateinit var etApellidoPaterno : EditText
private lateinit var etApellidoMaterno : EditText
private lateinit var etTelefono: EditText
private lateinit var etCorreo: EditText
private lateinit var etCalle: EditText
private lateinit var etNumero: EditText
private lateinit var etFecha: EditText
private lateinit var etPassword: EditText


class RegistroActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        etNombre = findViewById(R.id.etNombre)
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno)
        etTelefono = findViewById(R.id.etTelefono)
        etCorreo = findViewById(R.id.etCorreo)
        etCalle = findViewById(R.id.etCalle)
        etNumero = findViewById(R.id.etNumero)
        etFecha = findViewById<EditText>(R.id.etFechaNacimiento)
        etPassword = findViewById(R.id.etPassword)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnAceptar = findViewById<Button>(R.id.btnAceptar)
     etFecha.setOnClickListener {
        showDatePickerDialog()
     }
        btnCancelar.setOnClickListener {
            mandarIniciarSesion()
        }
        btnAceptar.setOnClickListener {
            validarCampos()
        }

    }
    private fun  showDatePickerDialog(){
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        etFecha.setText("$year-$month-$day")
    }

    private fun validarCampos(){
        val nombreTxt = etNombre.text.toString()
        val apellidoPaternoTxt = etApellidoPaterno.text.toString()
        val apellidoMaternoTxt = etApellidoMaterno.text.toString()
        val telefonoTxt = etTelefono.text.toString()
        val correoTxt = etCorreo.text.toString()
        val calleTxt = etCalle.text.toString()
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
        if(correoTxt.isEmpty()){
            valido = false
            etCorreo.setError("Campo telefono  requerido")
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
       enviarCredenciales(nombreTxt, apellidoPaternoTxt, apellidoMaternoTxt, telefonoTxt, correoTxt, calleTxt, numeroTxt, fechaTxt, passwordTxt)
        }
    }

    private fun mandarIniciarSesion(){
        val irVentanaIniciarSesion = Intent(this@RegistroActivity, MainActivity::class.java)
        startActivity(irVentanaIniciarSesion)
        finish()
    }
    private fun enviarCredenciales(nombre :String , apellidoPaterno:String, apellidoMaterno:String, telefono :String, correo :String, calle :String, numero:String, fecha:String, password:String){

        Ion.getDefault(this@RegistroActivity).conscryptMiddleware.enable(false)
        //implementacion de consumo
        Ion.with(this@RegistroActivity)
            .load("POST", Constantes.URL_WS+"usuarios/registrarUsuario")
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
                val nombreCompleto = "te has registrado " + respuestaWS.nombre+ " " + respuestaWS.apellidoPaterno + " " + respuestaWS.apellidoMaterno
                mostrarAlerta(nombreCompleto)
                val irInicioSesion = Intent(this@RegistroActivity, MainActivity::class.java)
                startActivity(irInicioSesion)
                finish()
            }else{
                mostrarAlerta(respuestaWS.mensaje)
            }
        }
    }

    private fun mostrarAlerta(mensaje: String){
        Toast.makeText(this@RegistroActivity, mensaje, Toast.LENGTH_LONG).show()
    }

}