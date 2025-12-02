package ca.unb.mobiledev.ontrack.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscription_table")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val serviceName: String,
    val amount: Double,
    val startDate: String,
    val billingCycle: String,
    val endDate: String? = null,
    val isActive: Boolean = true,
    val lastPaymentDate: String? = null,
    val cancelUrl: String? = null,
    val cancelledDate: String? = null
)