package de.sh.todoapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream

/**
 * Db helper class for managing the SQLite database.
 * This class is responsible for creating and managing the SQLite database.
 * @property context The application context.
 * @constructor Create empty Db helper class for managing the SQLite database.
 */
class DbHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Die Methode bleibt leer, da wir eine bestehende Datenbank aus den assets verwende.
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        context.deleteDatabase(DATABASE_NAME)
        copyDatabaseFromAssets()
    }

    override fun getReadableDatabase(): SQLiteDatabase {
        copyDatabaseFromAssets()
        return super.getReadableDatabase()
    }

    override fun getWritableDatabase(): SQLiteDatabase {
        copyDatabaseFromAssets()
        return super.getWritableDatabase()
    }

    private fun copyDatabaseFromAssets() {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        if (!dbPath.exists()) {
            try {
                context.assets.open(DATABASE_NAME).use { inputStream ->
                    FileOutputStream(dbPath).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                // Debug-Log : Erfolg
                android.util.Log.d("DbHelper", "Database copied successfully to: ${dbPath.absolutePath}")
                // Debug-Log Größe der Datei prüfen
                android.util.Log.d("DbHelper", "Database size: ${dbPath.length()} bytes")

            } catch (e: Exception) {
                //Debug-Log: Fail
                android.util.Log.e("DbHelper", "Error copying database", e)
            }
        }
    }

    companion object {
        const val DATABASE_NAME = "todo.db"
        const val DATABASE_VERSION = 1
    }
}