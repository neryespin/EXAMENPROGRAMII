package cl.nery.examen_ph.VM

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import cl.nery.examen_ph.DB.DataBase
import cl.nery.examen_ph.DB.Registro

class ClaseVM : ViewModel() {

    var itemList = mutableStateListOf<Registro>()

    suspend fun registrarMedicion(medida: String, fecha: String, opcion: String, contexto:Context) {
        if (!medida.isNullOrBlank() && !fecha.isNullOrBlank() && !opcion.isNullOrBlank()){
            var reg = Registro(medidor = medida.toInt(), fecha = fecha, option = opcion)
            itemList.add(reg)
            DataBase.getInstance(contexto).registroDao().insertarRegistro(reg)
        }
    }

    suspend fun mostrarListado(contexto: Context) {
        itemList.clear()
        itemList.addAll(DataBase.getInstance(contexto).registroDao().obtenerRegistro())
    }

    suspend fun borrarRegistro(contexto:Context, registro: Registro) {
        DataBase.getInstance(contexto).registroDao().borrarRegistro(registro)
        mostrarListado(contexto)
    }
}

