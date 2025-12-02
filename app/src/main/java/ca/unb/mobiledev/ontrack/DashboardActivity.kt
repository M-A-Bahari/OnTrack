// DashboardActivity.kt
package ca.unb.mobiledev.ontrack

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ontrack.entities.Subscription
import ca.unb.mobiledev.ontrack.ui.AddSubscriptionActivity
import ca.unb.mobiledev.ontrack.ui.SubscriptionAdapter
import ca.unb.mobiledev.ontrack.ui.SubscriptionViewModel

class DashboardActivity : AppCompatActivity() {

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_FIRST_NAME = "first_name"
    }

    private lateinit var totalPaidText: TextView
    private lateinit var monthlyAverageText: TextView
    private lateinit var welcomeText: TextView
    private lateinit var activeCountText: TextView
    private lateinit var addSubscriptionBtn: ImageButton
    private lateinit var logoutButton: ImageButton
    private lateinit var historyButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var subscriptionViewModel: SubscriptionViewModel
    private lateinit var userEmail: String
    private lateinit var firstName: String
    private lateinit var adapter: SubscriptionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Get user info - restore from savedInstanceState if available
        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString(KEY_USER_EMAIL) ?: ""
            firstName = savedInstanceState.getString(KEY_FIRST_NAME) ?: ""
        } else {
            userEmail = intent.getStringExtra("email") ?: ""
            firstName = intent.getStringExtra("firstName") ?: ""
        }

        // Initialize ViewModel
        subscriptionViewModel = ViewModelProvider(this)[SubscriptionViewModel::class.java]

        // Initialize UI elements
        welcomeText = findViewById(R.id.welcomeText)
        totalPaidText = findViewById(R.id.totalPaid)
        monthlyAverageText = findViewById(R.id.monthlyAverage)
        activeCountText = findViewById(R.id.activeCount)
        recyclerView = findViewById(R.id.subscriptionList)
        addSubscriptionBtn = findViewById(R.id.addSubscriptionBtn)
        logoutButton = findViewById(R.id.logoutButton)
        historyButton = findViewById(R.id.historyButton)

        // Set welcome message
        welcomeText.text = if (firstName.isNotEmpty()) {
            "Welcome back, $firstName!"
        } else {
            "Welcome back!"
        }

        // Setup RecyclerView
        adapter = SubscriptionAdapter(
            onItemClick = null // Click listener not needed for this step
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Setup swipe to delete/cancel
        setupSwipeToCancel()

        // Observe active subscriptions
        subscriptionViewModel.getActiveSubscriptions(userEmail).observe(this) { subscriptions ->
            adapter.submitList(subscriptions)
            
            // Update Active Count
            activeCountText.text = "${subscriptions.size} active"

            // Update Total Paid (sum of active subscription prices)
            val totalActivePrice = subscriptions.sumOf { it.amount }
            totalPaidText.text = "${String.format("%.2f", totalActivePrice)}"
        }

        // Observe total monthly cost (normalized)
        subscriptionViewModel.totalMonthlyCost.observe(this) { monthlyCost ->
            monthlyAverageText.text = "${String.format("%.2f", monthlyCost)}"
        }

        // Load stats from ViewModel
        subscriptionViewModel.loadDashboardStats(userEmail)

        // Add button
        addSubscriptionBtn.setOnClickListener {
            val intent = Intent(this, AddSubscriptionActivity::class.java)
            intent.putExtra("email", userEmail)
            startActivity(intent)
        }

        // History button
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("email", userEmail)
            startActivity(intent)
        }

        // Logout button
        logoutButton.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun setupSwipeToCancel() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // We don't need move functionality
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val subscription = adapter.getSubscriptionAt(position)
                
                // Show the first dialog
                showCancelSubscriptionDialog(subscription)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showCancelSubscriptionDialog(subscription: Subscription) {
        AlertDialog.Builder(this)
            .setTitle("Cancel Subscription")
            .setMessage("Do you want to visit the cancellation page for ${subscription.serviceName}?")
            .setPositiveButton("Yes, Visit Page") { _, _ ->
                openCancelUrl(subscription)
            }
            .setNegativeButton("No") { _, _ ->
                // Reset the item view if the user says no
                recyclerView.adapter?.notifyItemChanged(adapter.currentList.indexOf(subscription))
            }
            .setOnCancelListener {
                // Also reset if the dialog is dismissed
                recyclerView.adapter?.notifyItemChanged(adapter.currentList.indexOf(subscription))
            }
            .show()
    }

    private fun openCancelUrl(subscription: Subscription) {
        val url = subscription.cancelUrl ?: "https://www.google.com/search?q=how+to+cancel+${subscription.serviceName}"
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            // After returning from the browser, show the second dialog
            showPostCancellationDialog(subscription)
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open cancellation page.", Toast.LENGTH_SHORT).show()
            recyclerView.adapter?.notifyItemChanged(adapter.currentList.indexOf(subscription))
        }
    }

    private fun showPostCancellationDialog(subscription: Subscription) {
        AlertDialog.Builder(this)
            .setTitle("Cancellation Confirmation")
            .setMessage("Did you complete the cancellation for ${subscription.serviceName}?")
            .setPositiveButton("Yes, I Cancelled") { _, _ ->
                subscriptionViewModel.markAsInactive(subscription)
                Toast.makeText(this, "${subscription.serviceName} marked as inactive.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Not Yet") { _, _ ->
                recyclerView.adapter?.notifyItemChanged(adapter.currentList.indexOf(subscription))
            }
            .setCancelable(false) // User must choose an option
            .show()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh stats when returning to the activity
        subscriptionViewModel.loadDashboardStats(userEmail)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_USER_EMAIL, userEmail)
        outState.putString(KEY_FIRST_NAME, firstName)
    }
}