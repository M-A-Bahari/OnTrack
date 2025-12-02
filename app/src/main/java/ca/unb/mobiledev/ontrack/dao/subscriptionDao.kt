package ca.unb.mobiledev.ontrack.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ca.unb.mobiledev.ontrack.entities.Subscription

@Dao
interface SubscriptionDao {
    @Insert
    suspend fun insert(subscription: Subscription)

    @Update
    suspend fun update(subscription: Subscription)

    @Delete
    suspend fun delete(subscription: Subscription)

    // Get active subscriptions sorted by next payment date (ascending)
    @Query("""
        SELECT * FROM subscription_table 
        WHERE userEmail = :email AND isActive = 1 
        ORDER BY startDate ASC
    """)
    fun getActiveSubscriptions(email: String): LiveData<List<Subscription>>

    @Query("SELECT * FROM subscription_table WHERE userEmail = :email AND isActive = 0 ORDER BY startDate DESC")
    fun getInactiveSubscriptions(email: String): LiveData<List<Subscription>>

    @Query("SELECT * FROM subscription_table WHERE userEmail = :email ORDER BY startDate DESC")
    fun getAllSubscriptions(email: String): LiveData<List<Subscription>>

    @Query("SELECT * FROM subscription_table WHERE userEmail = :email")
    suspend fun getAllSubscriptionsSync(email: String): List<Subscription>

    @Query("SELECT * FROM subscription_table WHERE id = :id")
    suspend fun getSubscriptionById(id: Int): Subscription?

    @Query("""
        SELECT SUM(
            CASE billingCycle
                WHEN 'Weekly' THEN amount * 4.333
                WHEN 'Yearly' THEN amount / 12
                ELSE amount
            END
        )
        FROM subscription_table
        WHERE userEmail = :email AND isActive = 1
    """)
    suspend fun getTotalMonthlyCost(email: String): Double?
}