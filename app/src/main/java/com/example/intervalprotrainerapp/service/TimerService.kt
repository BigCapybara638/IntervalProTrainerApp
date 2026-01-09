package com.example.intervalprotrainerapp.service

import com.example.intervalprotrainerapp.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class TimerService : Service() {

    companion object {
        const val CHANNEL_ID = "CounterServiceChannel"
        const val NOTIFICATION_ID = 101
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_INCREMENT = "ACTION_INCREMENT"
        const val ACTION_RESET = "ACTION_RESET"

        const val EXTRA_END_VALUE = "end_value"
        const val EXTRA_INCREMENT_BY = "increment_by"
        const val EXTRA_SPEED = "speed"
    }

    private val counter = AtomicInteger(0)
    private var isRunning = false
    private var job: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.e("lifeCycle", "onCreate")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("lifeCycle", "onStartCommand")
        when(intent?.action) {
            ACTION_START -> {
                val endValue = intent.getIntExtra(EXTRA_END_VALUE, 60)
                start_counter(endValue)
                Log.e("lifeCycle", "ACTION_START")
            }
        }

        return START_STICKY
    }

    private fun start_counter(endValue: Int = 60) {
        counter.set(0)
        isRunning = true

        updateNotification()

        job = serviceScope.launch {
            for (i in 0 until endValue) {
                delay(1000L)
                counter.addAndGet(1)
                updateNotification()
                Log.e("lifeCycle", "start_counter")
            }
        }
    }

    private fun updateNotification(isPaused: Boolean = false) {
        val notification = buildNotification(isPaused)

        notificationManager.notify(NOTIFICATION_ID, notification)

        // Обновляем foreground notification
        if (isRunning || isPaused) {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun buildNotification(isPaused: Boolean = false): Notification {
        val currentValue = counter.get()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🔢 Счетчик работает ${if (isPaused) " (пауза)" else ""}")
            .setContentText("Текущее значение: $currentValue")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            //.setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true) // Не вибрировать при каждом обновлении
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("""
                    📊 Актуальный счетчик: $currentValue
                    ${if (isPaused) "⏸ Счетчик на паузе" else "▶️ Счетчик активен"}
                    
                """.trimIndent()))

        return builder.build()
    }


    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Таймер",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Актуальный счетчик"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}