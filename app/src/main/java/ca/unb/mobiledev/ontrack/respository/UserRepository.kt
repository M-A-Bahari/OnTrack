package ca.unb.mobiledev.ontrack.repository
import ca.unb.mobiledev.ontrack.dao.UserDao
import ca.unb.mobiledev.ontrack.entities.User
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    suspend fun registerUser(email: String, firstName: String, lastName: String, password: String): Boolean {
        return try {
            val hashedPassword = hashPassword(password)
            val user = User(email, firstName, lastName, hashedPassword)
            userDao.insert(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    suspend fun loginUser(email: String, password: String): User? {
        val hashedPassword = hashPassword(password)
        return userDao.login(email, hashedPassword)
    }
}