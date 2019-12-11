package ru.skillbranch.kotlinexample

import java.lang.IllegalArgumentException

/**
 * @author Andrei Khromov on 2019-12-10
 */
object UserHolder {
    private val map = mutableMapOf<String, User>()

    fun registerUser(fullName: String, email: String, password: String): User {
        return User.makeUser(fullName, email = email, password = password).also {
            user ->
            run {
                if (map[user.login] != null) {
                    throw IllegalArgumentException("A user with this email already exists")
                } else {
                    map[user.login] = user
                }
            }
        }
    }

    fun registerUserByPhone(fullName: String, rawPhone: String) : User {
        if (!rawPhone.matches("\\+([- _():=+]?\\d[- _():=+]?){11}(\\s*)?".toRegex())) {
            throw IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        }
        return User.makeUser(fullName, phone = rawPhone).also { user ->
            run {
                if (map[user.login]?.phone == rawPhone) {
                    throw IllegalArgumentException("A user with this phone already exists")
                } else {
                    map[user.login] = user
                }
            }
        }
    }

    fun loginUser(login: String, password: String): String? {
        return map[login.trim()]?.run {
            if (checkPassword(password)) {
                this.userInfo
            } else {
                null
            }
        }
    }

    fun requestAccessCode(login: String) : Unit {
        map[login]?.changeAccessCode()
    }
}