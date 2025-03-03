package com.andy.shiftmanager

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonthFilterActivity : AppCompatActivity() {

    private lateinit var dbHelper: ShiftDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShiftAdapter
    private lateinit var etMonth: EditText
    private lateinit var etYear: EditText
    private lateinit var tvSummary: TextView
    private var shiftList: List<Shift> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_filter)

        dbHelper = ShiftDatabaseHelper(this)

        etMonth = findViewById(R.id.et_Mese)
        etYear = findViewById(R.id.et_Anno)
        tvSummary = findViewById(R.id.tv_Riassunto)
        recyclerView = findViewById(R.id.rv_Filtrato)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inizializza l'adapter con una lista vuota e assegnalo alla RecyclerView
        adapter = ShiftAdapter(mutableListOf()) { selectedShift -> }
        recyclerView.adapter = adapter
        Log.d("MonthFilterActivity", "Adapter assegnato alla RecyclerView")

        // Recupera tutti i turni salvati nel database
        shiftList = dbHelper.getAllShifts()
        Log.d("MonthFilterActivity", "Numero di turni recuperati dal database: ${shiftList.size}")

        // Imposta il listener per filtrare automaticamente quando i campi cambiano
        etMonth.addTextChangedListener(filterWatcher)
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
        val monthInput = etMonth.text.toString()
        val year = etYear.text.toString()

        Log.d("MonthFilterActivity", "Input Mese: $monthInput, Input Anno: $year")

        if (monthInput.isEmpty() || year.isEmpty()) {
            Log.d("MonthFilterActivity", "Campi vuoti, azzeramento RecyclerView")
            updateRecyclerView(emptyList(), 0.0, 0.0)
            return
        }

        // Assicuriamoci che il mese sia formattato correttamente
        val month = monthInput.toIntOrNull()?.toString() ?: monthInput // Rimuove eventuali zeri iniziali
        Log.d("MonthFilterActivity", "Mese formattato: $month")

        val filteredList = shiftList.filter {
            val dateParts = it.dataOra.split("/")
            if (dateParts.size >= 3) {
                val storedDay = dateParts[0]
                val storedMonth = dateParts[1].toInt().toString() // Rimuove eventuali zeri iniziali
                val storedYear = dateParts[2]

                Log.d("MonthFilterActivity", "Confronto turno: ${it.dataOra} -> Estratti: Giorno: $storedDay, Mese: $storedMonth, Anno: $storedYear")
                storedMonth == month && storedYear == year
            } else {
                Log.d("MonthFilterActivity", "Formato data non valido per: ${it.dataOra}")
                false
            }
        }

        // Calcola il totale delle ore e della paga
        val totalHours = filteredList.sumOf { it.oreLavorate }
        val totalPay = filteredList.sumOf { it.oreLavorate * it.pagaOraria }

        Log.d("MonthFilterActivity", "Turni filtrati: ${filteredList.size}")
        updateRecyclerView(filteredList, totalHours, totalPay)
    }

    private fun updateRecyclerView(filteredList: List<Shift>, totalHours: Double, totalPay: Double) {
        (adapter as ShiftAdapter).updateShifts(filteredList)
        tvSummary.text = "Ore Totali: $totalHours | Paga Totale: €$totalPay"
        Log.d("MonthFilterActivity", "RecyclerView aggiornata con ${filteredList.size} elementi")
    }
}