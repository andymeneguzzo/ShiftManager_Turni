package com.andy.shiftmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var btnHome: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        btnHome = findViewById(R.id.fab_Home)

        // Bottone GitHub
        val btnGitHub: LinearLayout = findViewById(R.id.btn_github)
        btnGitHub.setOnClickListener {
            val githubUrl = "https://github.com/andymeneguzzo" // Sostituisci con il tuo link GitHub
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUrl))
            startActivity(intent)
        }

        // Bottone Giorno di Paga
        val btnGiornoPaga: TextView = findViewById(R.id.btn_giorno_paga)
        btnGiornoPaga.setOnClickListener {
            showDatePickerDialog(btnGiornoPaga)
        }

        btnHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendario = Calendar.getInstance()
        val anno = calendario.get(Calendar.YEAR)
        val mese = calendario.get(Calendar.MONTH)
        val giorno = calendario.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            textView.text = "Giorno di Paga: $selectedDate"
            Toast.makeText(this, "Giorno di paga aggiornato a: $selectedDate", Toast.LENGTH_SHORT).show()

            scheduleMonthlyNotification(dayOfMonth)

        }, anno, mese, giorno)

        datePicker.show()
    }

    private fun scheduleMonthlyNotification(dayOfMonth: Int) {
        val alarmManager = getSystemService(ALARM_SERVICE) as android.app.AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            this, 100, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
            set(Calendar.HOUR_OF_DAY, 9)  // Orario della notifica (9:00 AM)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.MONTH, 1) // Se la data è già passata questo mese, impostala per il mese successivo
            }
        }

        alarmManager.setRepeating(
            android.app.AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            android.app.AlarmManager.INTERVAL_DAY * 30, // Ripeti ogni mese
            pendingIntent
        )
    }
}