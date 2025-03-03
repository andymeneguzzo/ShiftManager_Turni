package com.andy.shiftmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var btnAggiungiTurno: MaterialButton
    private lateinit var btnVisualizzaTurni: MaterialButton
    private lateinit var btnStatistiche: MaterialButton
    private lateinit var btnImpostazioni: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Chiedi il permesso per le notifiche se necessario
        requestNotificationPermission()
        createNotificationChannel()


        btnAggiungiTurno = findViewById(R.id.btn_AggiungiTurno)
        btnVisualizzaTurni = findViewById(R.id.btn_VisualizzaTurni)
        btnStatistiche = findViewById(R.id.btn_Statistiche)
        btnImpostazioni = findViewById(R.id.fab_Impostazioni)

        btnAggiungiTurno.setOnClickListener {
            startActivity(Intent(this, AddShiftActivity::class.java))
        }

        btnVisualizzaTurni.setOnClickListener {
            startActivity(Intent(this, VisualizeShiftActivity::class.java))
        }

        btnStatistiche.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
        }

        btnImpostazioni.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notifiche abilitate!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifiche disabilitate. Puoi abilitarle dalle impostazioni.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Payday notification"
            val descriptionText = "Notifiche per il giorno di paga"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("payday_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}