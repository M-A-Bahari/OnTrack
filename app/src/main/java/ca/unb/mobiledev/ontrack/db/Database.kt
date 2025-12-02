package ca.unb.mobiledev.ontrack.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ca.unb.mobiledev.ontrack.dao.PaymentHistoryDao
import ca.unb.mobiledev.ontrack.dao.SubscriptionDao
import ca.unb.mobiledev.ontrack.dao.UserDao
import ca.unb.mobiledev.ontrack.entities.paymentHistory
import ca.unb.mobiledev.ontrack.entities.Subscription
import ca.unb.mobiledev.ontrack.entities.User

@Database(
    entities = [User::class, Subscription::class, paymentHistory::class],
    version = 4, // Incremented version
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun subscriptionDao(): SubscriptionDao
    abstract fun paymentHistoryDao(): PaymentHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Migration from version 3 to 4
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE subscription_table ADD COLUMN cancelledDate TEXT")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ontrack_database"
                )
                    .addMigrations(MIGRATION_3_4) // Add the migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}