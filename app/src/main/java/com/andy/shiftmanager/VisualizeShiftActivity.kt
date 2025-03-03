package com.andy.shiftmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class VisualizeShiftActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShiftAdapter
    private lateinit var dbHelper: ShiftDatabaseHelper
    private lateinit var btnFiltrare: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualize_shift)

        dbHelper = ShiftDatabaseHelper(this)
        btnFiltrare = findViewById(R.id.fab_Filtrare)

        recyclerView = findViewById(R.id.recyclerViewShifts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadShifts()

        btnFiltrare.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }
    }

    private fun loadShifts() {
        val shiftList = dbHelper.getAllShifts()
        adapter = ShiftAdapter(shiftList) { selectedShift ->
            val intent = Intent(this, EditShiftActivity::class.java).apply {
                putExtra("SHIFT_ID", selectedShift.id)
                putExtra("SHIFT_DATA_ORA", selectedShift.dataOra)
                putExtra("SHIFT_ORE", selectedShift.oreLavorate)
                putExtra("SHIFT_PAGA", selectedShift.pagaOraria)
                putExtra("SHIFT_NOTE", selectedShift.note)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadShifts() // Ricarica i dati quando si torna indietro da EditShiftActivity
    }
}