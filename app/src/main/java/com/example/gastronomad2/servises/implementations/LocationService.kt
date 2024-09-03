package com.example.gastronomad2.servises.implementations

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.gastronomad2.R
import com.example.gastronomad2.servises.db.implementations.UserDbApi
import com.example.gastronomad2.ui.screens.home.HomePageViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: DefaultLocationClient
    private val userDbApi = UserDbApi()
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification =  NotificationCompat.Builder(this,"running_channel")
            .setContentTitle("Tracing location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        startForeground(1,notification.build())

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(5000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                locationUpdates.value = location
                userDbApi.updateUserLocation(
                    CurrentUserInfo.getInstance().get()!!.id,
                    GeoPoint(location.latitude, location.longitude)
                )
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                   "Location: (${lat}, ${long})"
               )
                notificationManager.notify(1,updatedNotification.build())
            }.launchIn(serviceScope)
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        var locationUpdates = MutableStateFlow<Location?>(null)
    }
}