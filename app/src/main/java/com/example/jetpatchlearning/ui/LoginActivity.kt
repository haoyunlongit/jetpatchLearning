package com.example.jetpatchlearning.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.bridge.state.LoginModel
import com.example.jetpatchlearning.databinding.LoginActiviyBinding
import com.example.jetpatchlearning.ui.base.BaseActivity

class LoginActivity : BaseActivity() {
    var loginModel: LoginModel? = null

    var binding: LoginActiviyBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideActionBar()

        loginModel = getActivityViewModelProvider(this).get(LoginModel::class.java)

        binding =  DataBindingUtil.setContentView(this, R.layout.login_activiy)

        binding?.lifecycleOwner = this
        binding?.vm = loginModel
        binding?.click = ClickClass()

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

        }
    }

}