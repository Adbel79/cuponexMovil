package com.example.appcuponex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcuponex.poko.Promocion

class CustomAdapter : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    lateinit var listaPromocion : ArrayList<Promocion>
    var context: Context? = null




    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder:  ViewHolder, i: Int) {

        viewHolder.itemTitulo.text = listaPromocion.get(i).nombrePromocion
        viewHolder.itemVigenciaInicio.text = listaPromocion.get(i).fechaInicio
        //viewHolder.itemTipoPromocion.text = listaPromocion.get(i).idTipoPromocion.toString()

    }

    override fun getItemCount(): Int {
        return listaPromocion!!.count()

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemTitulo: TextView
        var itemVigenciaInicio : TextView
        //var itemTipoPromocion : TextView
        //var itemBotonDetalles: Button

        init{
            itemTitulo = itemView.findViewById(R.id.name_promocion_card)
           // itemEmpresa = itemView.findViewById(R.id.item_empresa)
            itemVigenciaInicio = itemView.findViewById(R.id.vigencia_promocion_card)
            //itemVigenciaTermino = itemView.findViewById(R.id.item_vigenciaTermino)
            //itemTipoPromocion = itemView.findViewById(R.id.empresa_promocion_card)
           // itemBotonDetalles = itemView.findViewById(R.id.btnPromocion)
        }

    }


}