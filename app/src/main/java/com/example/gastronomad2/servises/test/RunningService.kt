package com.example.gastronomad2.servises.test

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.gastronomad2.R
import kotlinx.coroutines.flow.MutableStateFlow

class RunningService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            START -> start()
            STOP -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, "running_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("eLAPSED TIME: 00:50")
            .build()
        startForeground(1,notification)
    }

    companion object {
        const val START = "ACTION_START"
        const val STOP = "ACTION_STOP" }
}
