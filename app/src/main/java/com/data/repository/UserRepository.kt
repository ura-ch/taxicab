package com.data.repository
import com.data.database.UserDao
import com.data.model.User
 import kotlinx.coroutines.Dispatchers
 import kotlinx.coroutines.withContext
class UserRepository(private val userDao: UserDao) {
    suspend fun insertUser(user: User) = withContext(Dispatchers.IO){ userDao.insertUser(user) }
    // Add functions for working with data
}