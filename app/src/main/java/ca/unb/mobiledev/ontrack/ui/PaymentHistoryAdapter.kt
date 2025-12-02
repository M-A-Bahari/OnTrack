// ui/PaymentHistoryAdapter.kt
package ca.unb.mobiledev.ontrack.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ontrack.R
import ca.unb.mobiledev.ontrack.entities.paymentHistory
import java.text.SimpleDateFormat
import java.util.*

class PaymentHistoryAdapter(
    private val payments: List<paymentHistory>
) : RecyclerView.Adapter<PaymentHistoryAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.payment_history, parent, false)
        return PaymentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = payments[position]

        holder.serviceName.text = payment.serviceName
        holder.amount.text = "$${String.format("%.2f", payment.amount)}"
        holder.paymentDate.text = formatDate(payment.paymentDate)
        holder.billingCycle.text = payment.billingCycle
        holder.serviceIcon.text = getEmojiForService(payment.serviceName)

        // Show payment status
        holder.status.text = "✓ Paid"
        holder.status.setBackgroundResource(R.drawable.buttonborder)
    }

    override fun getItemCount() = payments.size

    private fun formatDate(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateStr)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            dateStr
        }
    }

    private fun getEmojiForService(serviceName: String): String {
        return when (serviceName.lowercase()) {
            "netflix" -> "🎬"
            "spotify" -> "🎵"
            "amazon prime" -> "📦"
            "disney+" -> "🏰"
            "youtube premium" -> "▶️"
            "apple music" -> "🎧"
            "hbo max" -> "📺"
            else -> "📱"
        }
    }

    class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serviceIcon: TextView = itemView.findViewById(R.id.serviceIcon)
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val paymentDate: TextView = itemView.findViewById(R.id.paymentDate)
        val billingCycle: TextView = itemView.findViewById(R.id.billingCycle)
        val status: TextView = itemView.findViewById(R.id.status)
    }
}