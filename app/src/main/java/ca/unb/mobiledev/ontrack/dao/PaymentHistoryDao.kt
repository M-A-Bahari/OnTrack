package ca.unb.mobiledev.ontrack.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ca.unb.mobiledev.ontrack.entities.paymentHistory

@Dao
interface PaymentHistoryDao {
    @Insert
    suspend fun insert(payment: paymentHistory)

    @Query("SELECT * FROM payment_history_table WHERE userEmail = :email ORDER BY paymentDate DESC")
    fun getAllPayments(email: String): LiveData<List<paymentHistory>>

    @Query("SELECT SUM(amount) FROM payment_history_table WHERE userEmail = :email")
    suspend fun getTotalPaid(email: String): Double?

    @Query("SELECT AVG(amount) FROM payment_history_table WHERE userEmail = :email AND billingCycle = 'Monthly'")
    suspend fun getMonthlyAverage(email: String): Double?

    @Query("SELECT SUM(amount) FROM payment_history_table WHERE userEmail = :email AND strftime('%Y', paymentDate) = strftime('%Y', 'now')")
    suspend fun getYearlyTotal(email: String): Double?
}