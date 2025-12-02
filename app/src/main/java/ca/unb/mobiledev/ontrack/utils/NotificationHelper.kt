package ca.unb.mobiledev.ontrack.utils
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ca.unb.mobiledev.ontrack.DashboardActivity
import ca.unb.mobiledev.ontrack.R

object NotificationHelper {
    private const val CHANNEL_ID = "subscription_reminders"
    private const val CHANNEL_NAME = "Subscription Reminders"
    private const val CHANNEL_DESC = "Notifications for upcoming subscription payments"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showPaymentReminder(
        context: Context,
        serviceName: String,
        amount: Double,
        daysLeft: Int,
        notificationId: Int
    ) {
        // Check notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val message = when (daysLeft) {
            0 -> "Your $serviceName payment of $${"%.2f".format(amount)} is due today!"
            1 -> "Your $serviceName payment of $${"%.2f".format(amount)} is due tomorrow!"
            else -> "Your $serviceName payment of $${"%.2f".format(amount)} is due in $daysLeft days!"
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("Payment Reminder")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun sendEmailReminder(context: Context, serviceName: String, amount: Double, daysLeft: Int, userEmail: String) {
        val subject = "Payment Reminder: $serviceName"
        val body = """
            Hi there!
            
            This is a friendly reminder that your $serviceName subscription payment of $${"%.2f".format(amount)} is due in $daysLeft days.
            
            Please ensure you have sufficient funds available.
            
            Best regards,
            OnTrack Team
        """.trimIndent()

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(userEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send reminder via..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}