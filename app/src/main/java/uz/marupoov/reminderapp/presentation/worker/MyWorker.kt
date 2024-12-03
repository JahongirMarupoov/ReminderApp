package uz.marupoov.reminderapp.presentation.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import uz.marupoov.reminderapp.MainActivity
import uz.marupoov.reminderapp.R
import uz.marupoov.reminderapp.utils.isVisibleActivity
import uz.marupoov.reminderapp.utils.makeToast
import uz.marupoov.reminderapp.utils.myLogger
import uz.marupoov.reminderapp.utils.timeChangeFlow

class MyWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    private val chanelId = "reminder_chanel_id"
    private val notificationId = 1
    private val chanelName = "reminder_chanel_name"
    override fun doWork(): Result {
        val resultString = workerParameters.inputData.getString("TASK")
        myLogger("task" + resultString.toString(), "WORK")
        if (isVisibleActivity) {
            makeToast.tryEmit("It's time to complete the task !")
        } else {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder = NotificationCompat.Builder(context, chanelId)
                .setSmallIcon(R.drawable.reminder_logo)
                .setContentTitle("The task must be completed !")
                .setContentText(resultString)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            } else {
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            builder.setContentIntent(pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    chanelId,
                    chanelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                val ringtoneManager =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes =
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                channel.setSound(ringtoneManager, audioAttributes)
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(notificationId, builder.build())
        }
        timeChangeFlow.tryEmit(Unit)
        return Result.success()
    }
}