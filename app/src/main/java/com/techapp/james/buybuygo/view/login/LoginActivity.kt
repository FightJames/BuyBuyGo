package com.techapp.james.buybuygo.view.login

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.presenter.LoginPresenter
import com.techapp.james.buybuygo.view.BaseActivity
import com.techapp.james.buybuygo.view.choose.ChooseActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : BaseActivity(), LoginView {
    override fun showMessage(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG)
            .show()
    }

    override fun intentToChoose() {
        var i = Intent(this@LoginActivity, ChooseActivity::class.java)
        startActivity(i)
    }

    override fun isLoad(flag: Boolean) {
        if (flag) {
            progressBar.visibility = android.view.View.VISIBLE
        } else {
            progressBar.visibility = android.view.View.INVISIBLE
        }
    }

    lateinit var callbackManager: CallbackManager
    var loginPresenter: LoginPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d("hash " + getHash())
        init()
    }

    fun init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        loginPresenter = LoginPresenter(this)
        Glide.with(this)
            .load(R.drawable.buy_for_me)
            .into(backgroundImageView)
        callbackManager = CallbackManager.Factory.create()
        loginButton.setReadPermissions(arrayListOf("email"))
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                result.let {
                    Timber.d("result ${result.accessToken.token} ${result.accessToken.dataAccessExpirationTime}")
                    loginPresenter!!.onLoginSuccess(
                        result.accessToken.token
                        , result.accessToken.dataAccessExpirationTime.toString()
                    )
                }
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
                Timber.d("error ${error.toString()}")
            }
        })
    }

    fun getHash() {
        try {
            val info = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter = null
        LoginManager.getInstance().logOut()
        // clear sharPreference
    }
}
