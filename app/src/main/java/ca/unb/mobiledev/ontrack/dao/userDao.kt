package ca.unb.mobiledev.ontrack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ca.unb.mobiledev.ontrack.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?
}