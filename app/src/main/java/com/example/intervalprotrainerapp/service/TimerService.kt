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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.intervalprotrainerapp.models.TrainingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

enum class TimerState {
    WORK, RELAX
}

class TimerService : Service() {

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    companion object {
        const val CHANNEL_ID = "CounterServiceChannel"
        const val NOTIFICATION_ID = 101
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_INCREMENT = "ACTION_INCREMENT"
        const val ACTION_RESET = "ACTION_RESET"

        const val ACTION_TIMER_UPDATE = "ACTION_TIMER_UPDATE"
        const val EXTRA_COUNT = "EXTRA_COUNT"
        const val EXTRA_INTERVAL = "EXTRA_INTERVAL"

        const val EXTRA_TRAINING = "training"
        const val EXTRA_INCREMENT_BY = "increment_by"
        const val EXTRA_SPEED = "speed"
    }

    private val counter = AtomicInteger(0)
    private var isRunning = false

    private var state = TimerState.WORK
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
                val training = intent.getParcelableExtra<TrainingItem>(EXTRA_TRAINING)
                start_counter(training!!)
                Log.e("lifeCycle", "ACTION_START")
            }
        }

        return START_STICKY
    }

    private fun start_counter(training: TrainingItem) {
        counter.set(0)
        isRunning = true

        updateNotification()

        job = serviceScope.launch {
            for (i in 0 until training.cycles * 2 - 1) {
                when(state) {
                    TimerState.WORK -> {
                        for (j in 1 .. training.intervalWork) {
                            delay(1000L)
                            counter.addAndGet(1)
                            updateNotification()
                            Log.e("lifeCycle", "counterWork = $j")
                            val intent = Intent(ACTION_TIMER_UPDATE).apply {
                                putExtra(EXTRA_COUNT, j)
                                putExtra(EXTRA_INTERVAL, training.intervalWork)
                                putExtra("state", state.name)
                            }
                            localBroadcastManager.sendBroadcast(intent)
                        }
                        counter.set(0)
                        state = TimerState.RELAX
                    }
                    TimerState.RELAX -> {
                        for (j in 1 .. training.intervalRelax) {
                            delay(1000L)
                            counter.addAndGet(1)
                            updateNotification()
                            Log.e("lifeCycle", "counterRelax = $j")
                            val intent = Intent(ACTION_TIMER_UPDATE).apply {
                                putExtra(EXTRA_COUNT, j)
                                putExtra(EXTRA_INTERVAL, training.intervalRelax)
                                putExtra("state", state.name)
                            }
                            localBroadcastManager.sendBroadcast(intent)
                        }
                        counter.set(0)
                        state = TimerState.WORK
                    }
                }
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