package com.andy.shiftmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ShiftDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATA_ORA TEXT,
                $COLUMN_ORE_LAVORATE REAL,
                $COLUMN_PAGA_ORARIA REAL,
                $COLUMN_NOTE TEXT
            );
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun getAllShifts(): List<Shift> {
        val shifts = mutableListOf<Shift>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        while (cursor.moveToNext()) {
            val shift = Shift(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                dataOra = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATA_ORA)),
                oreLavorate = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ORE_LAVORATE)),
                pagaOraria = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PAGA_ORARIA)),
                note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
            )
            shifts.add(shift)
        }

        cursor.close()
        db.close()
        return shifts
    }

    fun insertShift(shift: Shift): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATA_ORA, shift.dataOra)
            put(COLUMN_ORE_LAVORATE, shift.oreLavorate)
            put(COLUMN_PAGA_ORARIA, shift.pagaOraria)
            put(COLUMN_NOTE, shift.note)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun updateShift(shift: Shift): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATA_ORA, shift.dataOra)
            put(COLUMN_ORE_LAVORATE, shift.oreLavorate)
            put(COLUMN_PAGA_ORARIA, shift.pagaOraria)
            put(COLUMN_NOTE, shift.note)
        }

        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(shift.id.toString()))
        db.close()
        return result > 0
    }

    fun deleteShift(shiftId: Long): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(shiftId.toString()))
        db.close()
        return result > 0
    }

    companion object {
        private const val DATABASE_NAME = "shifts.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "shifts"
        const val COLUMN_ID = "id"
        const val COLUMN_DATA_ORA = "data_ora"
        const val COLUMN_ORE_LAVORATE = "ore_lavorate"
        const val COLUMN_PAGA_ORARIA = "paga_oraria"
        const val COLUMN_NOTE = "note"
    }
}