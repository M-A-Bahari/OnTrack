package ca.unb.mobiledev.ontrack.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ontrack.R
import ca.unb.mobiledev.ontrack.entities.Subscription
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SubscriptionAdapter(
    private val onItemClick: ((Subscription) -> Unit)? = null
) : ListAdapter<Subscription, SubscriptionAdapter.SubscriptionViewHolder>(SubscriptionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_subscription, parent, false)
        return SubscriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubscriptionViewHolder, position: Int) {
        val subscription = getItem(position)

        // Service name
        holder.serviceName.text = subscription.serviceName

        // Service icon (emoji)
        holder.serviceIcon.text = getEmojiForService(subscription.serviceName)

        // Amount and billing cycle
        holder.amount.text = "$${String.format("%.0f", subscription.amount)}"
        holder.billingCycle.text = when (subscription.billingCycle) {
            "Monthly" -> "/month"
            "Yearly" -> "/year"
            "Weekly" -> "/week"
            else -> ""
        }

        // Category
        holder.category.text = getCategoryForService(subscription.serviceName)

        // Calculate next payment date
        val nextPayment = calculateNextPayment(subscription.startDate, subscription.billingCycle)
        holder.nextPaymentDate.text = "Next payment: ${formatDate(nextPayment)}"

        // Calculate days until payment
        val daysUntil = calculateDaysUntilPayment(nextPayment)

        // Show days until payment
        when {
            daysUntil < 0 -> {
                holder.daysUntilRenewal.text = "Overdue"
                holder.daysUntilRenewal.visibility = View.VISIBLE
            }
            daysUntil == 0 -> {
                holder.daysUntilRenewal.text = "Due today"
                holder.daysUntilRenewal.visibility = View.VISIBLE
            }
            daysUntil <= 3 -> {
                holder.daysUntilRenewal.text = "$daysUntil days left"
                holder.daysUntilRenewal.visibility = View.VISIBLE
            }
            else -> {
                holder.daysUntilRenewal.visibility = View.GONE
            }
        }

        // Click listener
        holder.cardView.setOnClickListener {
            onItemClick?.invoke(subscription)
        }
    }

    fun getSubscriptionAt(position: Int): Subscription = getItem(position)

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

    private fun calculateNextPayment(startDate: String, cycle: String): String {
        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val start = dateFormat.parse(startDate) ?: return startDate

            val calendar = Calendar.getInstance()
            calendar.time = start

            val today = Calendar.getInstance()

            // Find the next payment date after today
            while (calendar.before(today) || calendar.equals(today)) {
                when (cycle) {
                    "Weekly" -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
                    "Monthly" -> calendar.add(Calendar.MONTH, 1)
                    "Yearly" -> calendar.add(Calendar.YEAR, 1)
                    else -> return startDate
                }
            }

            return dateFormat.format(calendar.time)
        } catch (e: Exception) {
            return startDate
        }
    }

    private fun calculateDaysUntilPayment(paymentDate: String): Int {
        return try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val payment = dateFormat.parse(paymentDate) ?: return -1

            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)

            val diff = payment.time - today.timeInMillis
            TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        } catch (e: Exception) {
            -1
        }
    }

    private fun getCategoryForService(serviceName: String): String {
        return when (serviceName.lowercase()) {
            "netflix", "disney+", "hbo max", "hulu" -> "Entertainment"
            "spotify", "apple music", "youtube music" -> "Music"
            "amazon prime" -> "Shopping"
            "youtube premium" -> "Video"
            else -> "Other"
        }
    }

    class SubscriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val serviceName: TextView = itemView.findViewById(R.id.serviceName)
        val serviceIcon: TextView = itemView.findViewById(R.id.serviceIcon)
        val category: TextView = itemView.findViewById(R.id.category)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val billingCycle: TextView = itemView.findViewById(R.id.billingCycle)
        val nextPaymentDate: TextView = itemView.findViewById(R.id.nextPaymentDate)
        val daysUntilRenewal: TextView = itemView.findViewById(R.id.daysUntilRenewal)
    }
}

class SubscriptionDiffCallback : DiffUtil.ItemCallback<Subscription>() {
    override fun areItemsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Subscription, newItem: Subscription): Boolean {
        return oldItem == newItem
    }
}