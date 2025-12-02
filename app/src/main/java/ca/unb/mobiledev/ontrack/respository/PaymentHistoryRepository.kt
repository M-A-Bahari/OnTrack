package ca.unb.mobiledev.ontrack.repository
import androidx.lifecycle.LiveData
import ca.unb.mobiledev.ontrack.dao.PaymentHistoryDao
import ca.unb.mobiledev.ontrack.entities.paymentHistory

class PaymentHistoryRepository(private val paymentHistoryDao: PaymentHistoryDao) {

    fun getAllPayments(email: String): LiveData<List<paymentHistory>> {
        return paymentHistoryDao.getAllPayments(email)
    }

    suspend fun insert(payment: paymentHistory) {
        paymentHistoryDao.insert(payment)
    }

    suspend fun getTotalPaid(email: String): Double {
        return paymentHistoryDao.getTotalPaid(email) ?: 0.0
    }

    suspend fun getMonthlyAverage(email: String): Double {
        return paymentHistoryDao.getMonthlyAverage(email) ?: 0.0
    }

    suspend fun getYearlyTotal(email: String): Double {
        return paymentHistoryDao.getYearlyTotal(email) ?: 0.0
    }
}