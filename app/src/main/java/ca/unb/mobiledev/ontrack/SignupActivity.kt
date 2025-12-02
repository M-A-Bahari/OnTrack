package ca.unb.mobiledev.ontrack

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.unb.mobiledev.ontrack.ui.LoginViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        // Initialize ViewModel
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        // Initialize UI elements
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        confirmPassword = findViewById(R.id.signup_confirm)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        // Observe registration result
        loginViewModel.registerResult.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Signup successful! Please login.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Generic failure message, specific errors are handled by registrationError
                Toast.makeText(this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe for specific registration errors
        loginViewModel.registrationError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        // Sign Up button click listener
        signupButton.setOnClickListener {
            val firstNameText = firstName.text.toString().trim()
            val lastNameText = lastName.text.toString().trim()
            val emailText = email.text.toString().trim()
            val passwordText = password.text.toString().trim()
            val confirmText = confirmPassword.text.toString().trim()

            // Validation checks
            if (firstNameText.isEmpty() || lastNameText.isEmpty() ||
                emailText.isEmpty() || passwordText.isEmpty() || confirmText.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else if (passwordText != confirmText) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Call the centralized registration function in the ViewModel
                loginViewModel.attemptRegistration(emailText, firstNameText, lastNameText, passwordText)
            }
        }

        // Redirect to login
        loginRedirectText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}