package com.example.healthsense.data

import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

class PikerDate(context :Context) {

    companion object{
        fun toDateFormat(fecha : String?): String?{
            return formatoFecha(fecha, "dd/MM/yyyy", "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        }

        private fun formatoFecha(fecha: String?, patronEntrada: String, patronSalida: String): String?{
            if (fecha == null) return null

            val pos = ParsePosition(0)
            var simpledateformat = SimpleDateFormat(patronEntrada)
            val date = simpledateformat.parse(fecha, pos) //Hacer un Date con la fecha recibido
            if (date == null) return null //Si no parseó bien, retornar null

            simpledateformat = SimpleDateFormat(patronSalida)
            return simpledateformat.format(date) //Retornar Date formateado con el formato de salida
        }
    }


    private val CERO = "0"
    private val BARRA = "/"
    private val cont = context

    //Calendario para obtener fecha & hora
    val c = Calendar.getInstance()

    //Fecha
    val mes = c[Calendar.MONTH]
    val dia = c[Calendar.DAY_OF_MONTH]
    val anio = c[Calendar.YEAR]


    fun obtenerFecha(v: TextView) {
        val recogerFecha = DatePickerDialog(cont,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val mesActual = month + 1
                    val diaFormateado =
                            if (dayOfMonth < 10) CERO + dayOfMonth.toString() else dayOfMonth.toString()
                    val mesFormateado =
                            if (mesActual < 10) CERO + mesActual.toString() else mesActual.toString()
                    v.setText(diaFormateado + BARRA + mesFormateado + BARRA + year)
                }, anio, mes, dia
        )
        recogerFecha.show()
    }
}