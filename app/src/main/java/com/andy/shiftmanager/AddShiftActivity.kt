package com.andy.shiftmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddShiftActivity : AppCompatActivity() {

    private lateinit var dbHelper: ShiftDatabaseHelper
    private lateinit var etDataOra: EditText
    private lateinit var etOreLavorate: EditText
    private lateinit var etPagaOraria: EditText
    private lateinit var etNote: EditText
    private lateinit var btnSalva: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shift)

        // Inizializzo Database helper
        dbHelper = ShiftDatabaseHelper(this)

        // Campi di input
        etDataOra = findViewById(R.id.et_DataOra)
        etOreLavorate = findViewById(R.id.et_OreLavorate)
        etPagaOraria = findViewById(R.id.et_PagaOraria)
        etNote = findViewById(R.id.et_Note)
        btnSalva = findViewById(R.id.fab_SalvaTurno)

        // Apri il DatePicker al click sulla data
        etDataOra.setOnClickListener {
            showDatePickerDialog()
        }

        // Salva il turno quando si preme il pulsante
        btnSalva.setOnClickListener {
            saveShift()
            startActivity(Intent(this, VisualizeShiftActivity::class.java))
        }
    }

    // MOSTRA CALENDARIO instanziato alla data odierna
    private fun showDatePickerDialog() {
        val calendario = Calendar.getInstance()
        val anno = calendario.get(Calendar.YEAR)
        val mese = calendario.get(Calendar.MONTH)
        val giorno = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val dataSelezionata = "$dayOfMonth/${month + 1}/$year"
            etDataOra.setText(dataSelezionata)
        }, anno, mese, giorno)

        datePicker.show()
    }

    // SALVA TURNO con informazioni e collegandosi a DBHelper
    private fun saveShift() {
        val dataOra = etDataOra.text.toString()
        val oreLavorate = etOreLavorate.text.toString().toDoubleOrNull()
        val pagaOraria = etPagaOraria.text.toString().toDoubleOrNull()
        val note = etNote.text.toString()

        if (dataOra.isBlank() || oreLavorate == null || pagaOraria == null) {
            Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            return
        }

        val shift = Shift(dataOra = dataOra, oreLavorate = oreLavorate, pagaOraria = pagaOraria, note = note)
        val result = dbHelper.insertShift(shift)

        if (result != -1L) {
            Toast.makeText(this, "Turno salvato!", Toast.LENGTH_SHORT).show()
            finish() // Chiudi l'activity dopo il salvataggio
        } else {
            Toast.makeText(this, "Errore nel salvataggio", Toast.LENGTH_SHORT).show()
        }
    }
}