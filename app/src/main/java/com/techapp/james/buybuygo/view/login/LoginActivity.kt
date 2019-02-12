package com.techapp.james.buybuygo.view.login

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.techapp.james.buybuygo.R
import com.techapp.james.buybuygo.model.sharePreference.SharePreference
import com.techapp.james.buybuygo.presenter.LoginPresenter
import com.techapp.james.buybuygo.view.BaseActivity
import com.techapp.james.buybuygo.view.View
import com.techapp.james.buybuygo.view.choose.ChooseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : BaseActivity(), View {
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

                    var singleBackEnd = loginPresenter!!.onLoginSuccess(
                        result.accessToken.token
                        , result.accessToken.dataAccessExpirationTime.toString()
                    )
                    singleBackEnd.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe {
                            progressBar.visibility = android.view.View.VISIBLE
                        }
                        .doOnSuccess {
                            progressBar.visibility = android.view.View.GONE
                            var i = Intent(this@LoginActivity, ChooseActivity::class.java)
                            startActivity(i)
                        }
                        .onErrorReturn(object : Function<Throwable, Response<ResponseBody>> {
                            override fun apply(t: Throwable): Response<ResponseBody> {
                                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG)
                                    .show()
                                var jsonObject = JSONObject()
                                jsonObject.put("expiresIn", "error!")
                                var body = ResponseBody.create(
                                    MediaType.parse("application/json"),
                                    jsonObject.toString()
                                )
                                Timber.d("error item return")
//                                 408 is timeout code
                                return Response.error<ResponseBody>(408, body)
                            }
                        })
                        .doOnEvent { body, throwable ->
                            //onErrorItemReturn will emmit a data which not include throwable to this

                            Timber.d("call")
                            progressBar.visibility = android.view.View.GONE
                        }
                        .subscribe()
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
    }
}
