package com.andy.shiftmanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditShiftActivity : AppCompatActivity() {

    private lateinit var dbHelper: ShiftDatabaseHelper
    private lateinit var etDataOra: EditText
    private lateinit var etOreLavorate: EditText
    private lateinit var etPagaOraria: EditText
    private lateinit var etNote: EditText
    private lateinit var btnSalva: FloatingActionButton
    private lateinit var btnElimina: FloatingActionButton

    private var shiftId: Long = -1  // ID del turno da modificare

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_shift)

        dbHelper = ShiftDatabaseHelper(this)

        // Inizializza campi di input
        etDataOra = findViewById(R.id.et_DataOra)
        etOreLavorate = findViewById(R.id.et_OreLavorate)
        etPagaOraria = findViewById(R.id.et_PagaOraria)
        etNote = findViewById(R.id.et_Note)
        btnSalva = findViewById(R.id.fab_ModificaTurno)
        btnElimina = findViewById(R.id.fab_EliminaTurno)

        // Riceve i dati dal turno selezionato
        shiftId = intent.getLongExtra("SHIFT_ID", -1)
        etDataOra.setText(intent.getStringExtra("SHIFT_DATA_ORA"))
        etOreLavorate.setText(intent.getDoubleExtra("SHIFT_ORE", 0.0).toString())
        etPagaOraria.setText(intent.getDoubleExtra("SHIFT_PAGA", 0.0).toString())
        etNote.setText(intent.getStringExtra("SHIFT_NOTE"))

        // Aprire il DatePicker quando si clicca sulla data
        etDataOra.setOnClickListener {
            showDatePickerDialog()
        }

        // Aggiornare il turno nel database quando si preme il pulsante
        btnSalva.setOnClickListener {
            updateShift()
        }

        btnElimina.setOnClickListener {
            deleteShift()
        }
    }

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

    private fun updateShift() {
        val dataOra = etDataOra.text.toString()
        val oreLavorate = etOreLavorate.text.toString().toDoubleOrNull()
        val pagaOraria = etPagaOraria.text.toString().toDoubleOrNull()
        val note = etNote.text.toString()

        if (dataOra.isBlank() || oreLavorate == null || pagaOraria == null) {
            Toast.makeText(this, "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
            return
        }

        val shift = Shift(id = shiftId, dataOra = dataOra, oreLavorate = oreLavorate, pagaOraria = pagaOraria, note = note)
        val success = dbHelper.updateShift(shift)

        if (success) {
            Toast.makeText(this, "Turno aggiornato!", Toast.LENGTH_SHORT).show()
            finish() // Chiude l'activity
        } else {
            Toast.makeText(this, "Errore nell'aggiornamento", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteShift() {
        if (shiftId == -1L) {
            Toast.makeText(this, "Errore: turno non trovato", Toast.LENGTH_SHORT).show()
            return
        }

        // Mostra il dialog di conferma
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Sei sicuro/a? \uD83E\uDD28")
        builder.setMessage("Eliminerai definitivamente il turno.")

        builder.setPositiveButton("Sono sicuro/a") { _, _ ->
            // Se l'utente conferma, elimina il turno
            val success = dbHelper.deleteShift(shiftId)

            if (success) {
                Toast.makeText(this, "Turno eliminato con successo!", Toast.LENGTH_SHORT).show()
                finish() // Chiude l'activity e torna alla lista
            } else {
                Toast.makeText(this, "Errore nell'eliminazione del turno", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Naah...") { dialog, _ ->
            dialog.dismiss() // Chiude il dialog senza eliminare nulla
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }
}