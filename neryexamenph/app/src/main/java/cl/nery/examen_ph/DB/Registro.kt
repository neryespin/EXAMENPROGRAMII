package cl.nery.examen_ph.DB

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity
data class Registro(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    var medidor: Int,
    var fecha:String,
    val option:String

)
