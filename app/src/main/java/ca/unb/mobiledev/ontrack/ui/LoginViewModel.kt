// ui/LoginViewModel.kt
package ca.unb.mobiledev.ontrack.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ca.unb.mobiledev.ontrack.db.AppDatabase
import ca.unb.mobiledev.ontrack.entities.User
import ca.unb.mobiledev.ontrack.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    val loginResult = MutableLiveData<User?>()
    val registerResult = MutableLiveData<Boolean>()
    val registrationError = MutableLiveData<String>() // For specific error messages

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.loginUser(email, password)
            loginResult.postValue(user)
        }
    }

    fun attemptRegistration(email: String, firstName: String, lastName: String, password: String) {
        viewModelScope.launch {
            val emailExists = userRepository.checkEmailExists(email)
            if (emailExists) {
                registrationError.postValue("Email already exists")
            } else {
                val success = userRepository.registerUser(email, firstName, lastName, password)
                registerResult.postValue(success)
            }
        }
    }
}