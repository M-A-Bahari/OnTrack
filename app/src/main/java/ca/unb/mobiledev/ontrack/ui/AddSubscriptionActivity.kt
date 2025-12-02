package ca.unb.mobiledev.ontrack.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ca.unb.mobiledev.ontrack.R
import ca.unb.mobiledev.ontrack.entities.Subscription
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddSubscriptionActivity : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var priceEditText: TextInputEditText
    private lateinit var startDateEditText: TextInputEditText
    private lateinit var billingCycleSpinner: Spinner
    private lateinit var cancelUrlEditText: TextInputEditText
    private lateinit var saveButton: Button

    private lateinit var subscriptionViewModel: SubscriptionViewModel
    private lateinit var userEmail: String
    private val calendar: Calendar = Calendar.getInstance()

    companion object {
        private const val KEY_NAME = "key_name"
        private const val KEY_PRICE = "key_price"
        private const val KEY_START_DATE = "key_start_date"
        private const val KEY_CANCEL_URL = "key_cancel_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_subscription)

        userEmail = intent.getStringExtra("email") ?: ""
        if (userEmail.isEmpty()) {
            Toast.makeText(this, "Error: User email not found.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        subscriptionViewModel = ViewModelProvider(this)[SubscriptionViewModel::class.java]

        // Initialize UI components
        nameEditText = findViewById(R.id.name_edit_text)
        priceEditText = findViewById(R.id.price_edit_text)
        startDateEditText = findViewById(R.id.start_date_edit_text)
        billingCycleSpinner = findViewById(R.id.billing_cycle_spinner)
        cancelUrlEditText = findViewById(R.id.cancel_url_edit_text)
        saveButton = findViewById(R.id.save_button)

        setupDatePicker()
        setupSpinner()

        saveButton.setOnClickListener {
            saveSubscription()
        }

        // Restore state if it exists
        savedInstanceState?.let {
            nameEditText.setText(it.getString(KEY_NAME))
            priceEditText.setText(it.getString(KEY_PRICE))
            startDateEditText.setText(it.getString(KEY_START_DATE))
            cancelUrlEditText.setText(it.getString(KEY_CANCEL_URL))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current state of the input fields
        outState.putString(KEY_NAME, nameEditText.text.toString())
        outState.putString(KEY_PRICE, priceEditText.text.toString())
        outState.putString(KEY_START_DATE, startDateEditText.text.toString())
        outState.putString(KEY_CANCEL_URL, cancelUrlEditText.text.toString())
    }

    private fun setupDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        startDateEditText.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupSpinner() {
        val cycles = listOf("Weekly", "Monthly", "Yearly")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cycles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        billingCycleSpinner.adapter = adapter
    }

    private fun updateDateInView() {
        val myFormat = "yyyy-MM-dd" // Correct format for database
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        startDateEditText.setText(sdf.format(calendar.time))
    }

    private fun saveSubscription() {
        val name = nameEditText.text.toString().trim()
        val priceStr = priceEditText.text.toString().trim()
        val startDate = startDateEditText.text.toString().trim()
        val billingCycle = billingCycleSpinner.selectedItem.toString()
        val cancelUrl = cancelUrlEditText.text.toString().trim()

        if (name.isEmpty() || priceStr.isEmpty() || startDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull()
        if (price == null || price <= 0) {
            Toast.makeText(this, "Please enter a valid price.", Toast.LENGTH_SHORT).show()
            return
        }

        val subscription = Subscription(
            userEmail = userEmail,
            serviceName = name,
            amount = price,
            startDate = startDate,
            billingCycle = billingCycle,
            cancelUrl = cancelUrl.ifEmpty { null }
        )

        subscriptionViewModel.addSubscription(subscription)

        Toast.makeText(this, "Subscription saved!", Toast.LENGTH_SHORT).show()
        finish() // Go back to the dashboard
    }
}
