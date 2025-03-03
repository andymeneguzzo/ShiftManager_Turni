package com.andy.shiftmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class StatisticsActivity : AppCompatActivity() {

    private lateinit var btnMonthFilter: MaterialButton
    private lateinit var btnYearFilter: MaterialButton
    private lateinit var btnSettings: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        btnMonthFilter = findViewById(R.id.btn_FiltroMese)
        btnYearFilter = findViewById(R.id.btn_FiltroAnno)
        btnSettings = findViewById(R.id.fab_Impostazioni)

        btnMonthFilter.setOnClickListener {
            startActivity(Intent(this, MonthFilterActivity::class.java))
        }

        btnYearFilter.setOnClickListener {
            startActivity(Intent(this, YearFilterActivity::class.java))
        }

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}