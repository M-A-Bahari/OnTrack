package ca.unb.mobiledev.ontrack.repository

import androidx.lifecycle.LiveData
import ca.unb.mobiledev.ontrack.dao.SubscriptionDao
import ca.unb.mobiledev.ontrack.entities.Subscription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubscriptionRepository(private val subscriptionDao: SubscriptionDao) {

    fun getActiveSubscriptions(email: String): LiveData<List<Subscription>> {
        return subscriptionDao.getActiveSubscriptions(email)
    }

    fun getInactiveSubscriptions(email: String): LiveData<List<Subscription>> {
        return subscriptionDao.getInactiveSubscriptions(email)
    }

    fun getAllSubscriptions(email: String): LiveData<List<Subscription>> {
        return subscriptionDao.getAllSubscriptions(email)
    }

    suspend fun getAllSubscriptionsSync(email: String): List<Subscription> {
        return withContext(Dispatchers.IO) {
            subscriptionDao.getAllSubscriptionsSync(email)
        }
    }

    suspend fun insert(subscription: Subscription) {
        subscriptionDao.insert(subscription)
    }

    suspend fun update(subscription: Subscription) {
        subscriptionDao.update(subscription)
    }

    suspend fun delete(subscription: Subscription) {
        subscriptionDao.delete(subscription)
    }

    suspend fun getSubscriptionById(id: Int): Subscription? {
        return subscriptionDao.getSubscriptionById(id)
    }

    suspend fun getTotalMonthlyCost(email: String): Double {
        return withContext(Dispatchers.IO) {
            subscriptionDao.getTotalMonthlyCost(email) ?: 0.0
        }
    }
}