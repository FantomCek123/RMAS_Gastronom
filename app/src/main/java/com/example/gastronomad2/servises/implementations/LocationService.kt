package com.example.gastronomad2.servises.implementations

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.gastronomad2.MainActivity
import com.example.gastronomad2.R
import com.example.gastronomad2.servises.db.DbApi
import com.example.gastronomad2.servises.db.implementations.UserDbApi
import com.example.gastronomad2.ui.screens.home.HomePageViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.cos

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: DefaultLocationClient
    private val notifiedRestaurant = mutableSetOf<String>()
    private val dbApi = DbApi()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> {
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                start()
            }

            ACTION_START_NEARBY -> {
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                start(true)
            }

            ACTION_STOP -> stop()
        }
        return START_NOT_STICKY
    }

    private fun start(nearbyService: Boolean = false) {
        locationClient
            .getLocationUpdates(1000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                locationUpdates.value = location
                if (nearbyService) {
                    sendNearbyLocationsNotification(location.latitude, location.longitude)
                }
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                "Location",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, "location")
            .setContentTitle("Praćenje lokacije...")
            .setContentText("Servis u pozadini prati vašu lokaciju.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun sendNearbyLocationsNotification(lat: Double, lng: Double) {
        val nearbyRestaurants = dbApi.getNearbyRestaurants(lat, lng, 0.1)
        for(restaurant in nearbyRestaurants)
        {
            Log.d("LocationService", "Nearby restaurants: ${nearbyRestaurants.size}")
            if(!notifiedRestaurant.contains(restaurant.id)) {
                Log.d("LocationService", "Sending notification for restaurant: ${restaurant.title}")
                sendNotification(restaurant.title!!)
                notifiedRestaurant.add(restaurant.id!!)
            }else
            {
                Log.d("LocationService", "Restaurant already notified: ${restaurant.title}")
            }
        }
    }

    private fun sendNotification(title: String) {
        val activityIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setContentTitle("Restoran u blizi")
            .setContentText("Nalazite se u blizini restorana: ${title}!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NEARBY_NOTIFICATION_ID, notification)
    }



    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_START_NEARBY = "ACTION_START_NEARBY"
        const val ACTION_STOP = "ACTION_STOP"
        const val NOTIFICATION_ID = 1
        const val NEARBY_NOTIFICATION_ID = 2
        const val NOTIFICATION_CHANNEL = "location"
        var locationUpdates = MutableStateFlow<Location?>(null)
    }
}