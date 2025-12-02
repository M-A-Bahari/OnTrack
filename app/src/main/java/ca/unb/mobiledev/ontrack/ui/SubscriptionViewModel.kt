package ca.unb.mobiledev.ontrack.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.unb.mobiledev.ontrack.db.AppDatabase
import ca.unb.mobiledev.ontrack.entities.Subscription
import ca.unb.mobiledev.ontrack.repository.SubscriptionRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SubscriptionViewModel(application: Application) : AndroidViewModel(application) {
    private val subscriptionRepository: SubscriptionRepository

    // LiveData for dashboard statistics based on ACTIVE subscriptions
    val totalMonthlyCost = MutableLiveData<Double>()

    init {
        val database = AppDatabase.getDatabase(application)
        subscriptionRepository = SubscriptionRepository(database.subscriptionDao())
    }

    fun getActiveSubscriptions(email: String): LiveData<List<Subscription>> {
        return subscriptionRepository.getActiveSubscriptions(email)
    }

    fun getInactiveSubscriptions(email: String): LiveData<List<Subscription>> {
        return subscriptionRepository.getInactiveSubscriptions(email)
    }

    fun addSubscription(subscription: Subscription) {
        viewModelScope.launch {
            subscriptionRepository.insert(subscription)
        }
    }

    fun markAsInactive(subscription: Subscription) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = sdf.format(Date())
            val updatedSubscription = subscription.copy(
                isActive = false,
                cancelledDate = currentDate
            )
            subscriptionRepository.update(updatedSubscription)
        }
    }

    fun loadDashboardStats(email: String) {
        viewModelScope.launch {
            val monthlyCost = subscriptionRepository.getTotalMonthlyCost(email)
            totalMonthlyCost.postValue(monthlyCost)
        }
    }
}