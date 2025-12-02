package ca.unb.mobiledev.ontrack

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.unb.mobiledev.ontrack.ui.SubscriptionAdapter
import ca.unb.mobiledev.ontrack.ui.SubscriptionViewModel
import com.google.android.material.appbar.MaterialToolbar

class HistoryActivity : AppCompatActivity() {

    companion object {
        private const val KEY_USER_EMAIL = "user_email"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var subscriptionViewModel: SubscriptionViewModel
    private lateinit var adapter: SubscriptionAdapter
    private lateinit var emptyHistoryText: TextView
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

        // Get user email - restore from savedInstanceState if available
        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString(KEY_USER_EMAIL) ?: ""
        } else {
            userEmail = intent.getStringExtra("email") ?: ""
        }

        if (userEmail.isEmpty()) {
            finish() // Can't function without the user's email
            return
        }

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        emptyHistoryText = findViewById(R.id.empty_history_text)
        recyclerView = findViewById(R.id.historyRecyclerView)
        adapter = SubscriptionAdapter() // Re-using the same adapter

        recyclerView.adapter = adapter

        // Set layout manager based on orientation
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        subscriptionViewModel = ViewModelProvider(this)[SubscriptionViewModel::class.java]

        subscriptionViewModel.getInactiveSubscriptions(userEmail).observe(this) { inactiveSubscriptions ->
            if (inactiveSubscriptions.isNullOrEmpty()) {
                recyclerView.visibility = View.GONE
                emptyHistoryText.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyHistoryText.visibility = View.GONE
                adapter.submitList(inactiveSubscriptions)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_USER_EMAIL, userEmail)
    }
}