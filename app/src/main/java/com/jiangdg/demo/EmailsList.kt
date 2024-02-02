package com.jiangdg.demo

class EmailsList {
    private var Role: String? = null
    private var name: String? = null
    private var Email: String? = null

    fun EmailsList() {
        // empty constructor
    }

    fun EmailsList(role: String?, email: String?, Name: String?) {
        Role = role
        Email = email
        name = Name
    }

    fun getRole(): String? {
        return Role
    }

    fun setRole(role: String?) {
        Role = role
    }

    fun getEmail(): String? {
        return Email
    }

    fun setEmail(email: String?) {
        Email = email
    }


    fun getName(): String? {
        return name
    }

    fun setName(Name: String?) {
        name = Name
    }

}