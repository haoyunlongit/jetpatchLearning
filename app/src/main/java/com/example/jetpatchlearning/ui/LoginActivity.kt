package com.example.jetpatchlearning.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.example.jetpatchlearning.MainActivity
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.bridge.request.RequestLoginViewModel
import com.example.jetpatchlearning.bridge.state.LoginModel
import com.example.jetpatchlearning.data.login_register.LoginRegisterResponse
import com.example.jetpatchlearning.data.login_register.Session
import com.example.jetpatchlearning.databinding.LoginActiviyBinding
import com.example.jetpatchlearning.ui.base.BaseActivity

class LoginActivity : BaseActivity() {
    var loginViewModel: LoginModel? = null

    var binding: LoginActiviyBinding? = null

    var requestLoginViewModel : RequestLoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideActionBar()
        requestLoginViewModel = getActivityViewModelProvider(this).get(RequestLoginViewModel::class.java)
        loginViewModel = getActivityViewModelProvider(this).get(LoginModel::class.java)

        binding =  DataBindingUtil.setContentView(this, R.layout.login_activiy)

        binding?.lifecycleOwner = this
        binding?.vm = loginViewModel
        binding?.click = ClickClass()

        requestLoginViewModel!!.registerData1!!.observe(this, {
            loginSuccess(it)
        })

        requestLoginViewModel!!.registerData2!!.observe(this, {
            loginFialure(it)
        })

    }

    private fun loginFialure(errorMsg: String?) {
        // Toast.makeText(this@LoginActivity, "登录失败 ~ 呜呜呜", Toast.LENGTH_SHORT).show()
        loginViewModel ?.loginState ?.value = "骚瑞 登录失败，错误信息是:${errorMsg}"
    }

    private fun loginSuccess(registerBean: LoginRegisterResponse?) {
        loginViewModel?.loginState?.value = "恭喜 ${registerBean?.username} 用户，登录成功"

        // 登录成功 在跳转首页之前，需要 保存 登录的会话
        // 保存登录的临时会话信息
        mSharedViewModel.session.value = Session(true, registerBean)

        // 跳转到首页
        startActivity(Intent(this@LoginActivity,  MainActivity::class.java))
    }

    inner class ClickClass {
        // 跳转到 注册界面
        fun startToRegister() {
            startActivity(
                Intent(this@LoginActivity,
                RegisterActivity::class.java)
            )
        }

        fun loginAction() {
            if (loginViewModel !!.userName.value.isNullOrBlank() || loginViewModel !!.userPwd.value.isNullOrBlank()) {
                loginViewModel ?.loginState ?.value = "用户名 或 密码 为空，请你好好检查"
                return
            }

            // 非协程版本
            /*requestLoginViewModel ?.requestLogin(
                this@LoginActivity,
                loginViewModel !!.userName.value!!,
                loginViewModel !!.userPwd.value!!,
                loginViewModel !!.userPwd.value!!
            )*/

            // 协程版本
            requestLoginViewModel ?.requestLoginCoroutine( this@LoginActivity,
                loginViewModel !!.userName.value!!, loginViewModel !!.userPwd.value!!)
        }
    }

}