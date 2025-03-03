package com.andy.shiftmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class YearFilterActivity : AppCompatActivity() {

    private lateinit var dbHelper: ShiftDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShiftAdapter
    private lateinit var etYear: EditText
    private lateinit var tvSummary: TextView
    private var shiftList: List<Shift> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year_filter)

        dbHelper = ShiftDatabaseHelper(this)

        etYear = findViewById(R.id.et_Anno)
        tvSummary = findViewById(R.id.tv_Riassunto)
        recyclerView = findViewById(R.id.rv_Filtrato)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera tutti i turni salvati nel database
        shiftList = dbHelper.getAllShifts()

        // Imposta il listener per filtrare automaticamente quando il campo cambia
        etYear.addTextChangedListener(filterWatcher)
    }

    private val filterWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            filterShifts()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun filterShifts() {
        val year = etYear.text.toString()

        if (year.isEmpty()) {
            updateRecyclerView(emptyList(), 0.0, 0.0)
            return
        }

        val filteredList = shiftList.filter {
            it.dataOra.endsWith(year) // Assumendo che la data sia salvata nel formato "dd/MM/yyyy"
        }

        // Calcola il totale delle ore e della paga
        val totalHours = filteredList.sumOf { it.oreLavorate }
        val totalPay = filteredList.sumOf { it.oreLavorate * it.pagaOraria }

        updateRecyclerView(filteredList, totalHours, totalPay)
    }

    private fun updateRecyclerView(filteredList: List<Shift>, totalHours: Double, totalPay: Double) {
        adapter = ShiftAdapter(filteredList) { selectedShift ->
            // Puoi implementare la modifica del turno se necessario
        }
        recyclerView.adapter = adapter
        tvSummary.text = "Ore Totali: $totalHours | Paga Totale: €$totalPay"
    }
}