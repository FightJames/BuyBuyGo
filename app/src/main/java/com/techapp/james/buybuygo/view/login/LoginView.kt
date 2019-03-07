package com.techapp.james.buybuygo.view.login

interface LoginView {
    fun isLoad(flag: Boolean)
    fun intentToChoose()
    fun showMessage(message:String)
    fun finish()
}