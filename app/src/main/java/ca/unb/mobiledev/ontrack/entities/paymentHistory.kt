package ca.unb.mobiledev.ontrack.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_history_table")
data class paymentHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userEmail: String,
    val subscriptionId: Int,
    val serviceName: String,
    val amount: Double,
    val paymentDate: String,
    val billingCycle: String
)