package Persistence

import com.innovatech.peaceapp.Alert.Beans.Alert
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class OpenHelper(context: Context): SQLiteOpenHelper(
    context, "alerts.db", null, 1
) {
    // Crear la tabla "alerts"
    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
            CREATE TABLE alerts (
                _ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                title TEXT, 
                location TEXT, 
                time TEXT
            )
        """.trimIndent()
        db!!.execSQL(query)
    }

    // Actualizar la tabla cuando la versión de la base de datos cambia
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query2 = "DROP TABLE IF EXISTS alerts"
        db!!.execSQL(query2)
        onCreate(db)
    }

    // Función para agregar una nueva alerta
    fun nuevaAlerta(alert: Alert) {
        val datos = ContentValues()
        datos.put("title", alert.title)
        datos.put("location", alert.location)
        datos.put("time", alert.time)
        this.writableDatabase.insert("alerts", null, datos)
    }

    // Función para leer todas las alertas
    fun leerAlertas(): MutableList<Alert> {
        val db = this.readableDatabase
        val query = "SELECT * FROM alerts"
        val result = db.rawQuery(query, null)
        val listaAlertas = mutableListOf<Alert>()
        if (result.moveToFirst()) {
            do {
                listaAlertas.add(
                    Alert(
                        result.getInt(0),  // ID
                        result.getString(1),  // Title
                        result.getString(2),  // Location
                        result.getString(3)   // Time
                    )
                )
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return listaAlertas
    }

    // Función para eliminar una alerta por su ID
    fun eliminarAlerta(id: Int) {
        val db = this.writableDatabase
        db.delete("alerts", "_ID = ?", arrayOf(id.toString()))
    }

    // Función para actualizar una alerta
    fun actualizarAlerta(alert: Alert) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("title", alert.title)
        contentValues.put("location", alert.location)
        contentValues.put("time", alert.time)
        db.update("alerts", contentValues, "_ID = ?", arrayOf(alert.id.toString()))
    }
}
