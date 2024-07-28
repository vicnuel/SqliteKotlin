package com.vicnuel.sqlitekotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context): SQLiteOpenHelper(context, "database.db", null, 1) {

    private val sql = arrayOf(
        "CREATE TABLE utilizador (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)",
        "INSERT INTO utilizador (username, password) VALUES ('user', 'pwd')",
        "INSERT INTO utilizador (username, password) VALUES ('admin', 'pwd')"
    )

    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE utilizador")
        onCreate(db)
    }

    fun utilizadorInsert(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put("username", username)
        contentValue.put("password", password)

        val l = db.insert("utilizador", null, contentValue)
        db.close()
        return l
    }

    fun utilizadorUpdate(id:Int, username: String, password: String): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put("username", username)
        contentValue.put("password", password)

        val res = db.update("utilizador", contentValue, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun utilizadorDelete(id:Int): Int {
        val db = this.writableDatabase
        val res = db.delete("utilizador", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun utilizadorSelectAll(): Cursor {
        val db = this.readableDatabase

        val cur = db.rawQuery("SELECT * FROM utilizador", null)

        db.close()

        return cur
    }

    fun utilizadorSelectById(id: Int): Cursor {
        val db = this.readableDatabase

        val cur = db.rawQuery("SELECT * FROM utilizadorn WHERE id=?", arrayOf(id.toString()))

        db.close()

        return cur
    }

    fun utilizadorObjectSelectById(id: Int): Utilizador {
        val db = this.readableDatabase

        val cur = db.rawQuery("SELECT * FROM utilizadorn WHERE id=?", arrayOf(id.toString()))

        db.close()

        var utilizador = Utilizador()

        if(cur.count == 1) {
            cur.moveToFirst()

                val idIndex = cur.getColumnIndex("id")
                val usernameIndex = cur.getColumnIndex("username")
                val passwordIndex = cur.getColumnIndex("password")

                val id = cur.getInt(idIndex)
                val username = cur.getString(usernameIndex)
                val password = cur.getString(passwordIndex)

                utilizador = Utilizador(id, username, password)
        }

        return utilizador
    }

    fun utilizadorListSelectAll(): ArrayList<Utilizador> {
        val db = this.readableDatabase

        val cur = db.rawQuery("SELECT * FROM utilizador", null)

        val listaUtilizador: ArrayList<Utilizador> = ArrayList()

        if (cur.count > 0) {
            cur.moveToFirst()
            do {
                val idIndex = cur.getColumnIndex("id")
                val usernameIndex = cur.getColumnIndex("username")
                val passwordIndex = cur.getColumnIndex("password")

                val id = cur.getInt(idIndex)
                val username = cur.getString(usernameIndex)
                val password = cur.getString(passwordIndex)

                listaUtilizador.add(Utilizador(id, username, password))
            } while (cur.moveToNext())
        }
        db.close()
        return listaUtilizador
    }
}