package cl.nery.examen_ph.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RegistroDao {

    @Insert
    suspend fun insertarRegistro(reg: Registro)

    @Query("SELECT * FROM registro")
    suspend fun obtenerRegistro():List<Registro>

    @Delete
    suspend fun borrarRegistro(reg:Registro)

}