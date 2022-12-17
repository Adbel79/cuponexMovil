package com.example.appcuponex.poko

class Promocion {
    var idPromocion: Int = 0
    var nombrePromocion : String = ""
    var foto :String = ""
    var descripcion: String  = " "
    var fechaInicio : String = " "
    var fechaTermino : String = " "
    var restricciones : String = " "
    var idTipoPromocion : Int = 0
    var porcentajeDescuento : Int = 0
    var costoPromocion : Float = 1.0f
    var idCategoria : Int = 0
    var idEstatus : Int = 0

    override fun toString(): String {
        return nombrePromocion
    }
}