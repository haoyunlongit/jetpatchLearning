package com.example.jetpatchlearning.ui

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.bridge.state.RegisterModel
import com.example.jetpatchlearning.bridge.state.RequestRegisterViewModel
import com.example.jetpatchlearning.data.login_register.LoginRegisterResponse
import com.example.jetpatchlearning.databinding.RegisterViewBinding
import com.example.jetpatchlearning.ui.base.BaseActivity

class RegisterActivity : BaseActivity() {

    var binding: RegisterViewBinding? = null

    var registerModel: RegisterModel? = null

    var requestRegisterViewModel: RequestRegisterViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerModel = getActivityViewModelProvider(this).get(RegisterModel::class.java)
        requestRegisterViewModel = getActivityViewModelProvider(this).get(RequestRegisterViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.register_view)
        binding?.lifecycleOwner = this
        binding?.vm = registerModel
        binding?.click = InnerClick()

        requestRegisterViewModel ?.registerData1 ?.observe(this, {
            registerSuccess(it)
        })
        requestRegisterViewModel ?.registerData2 ?.observe(this, {
            registerFailed(it)
        })
    }

    fun registerSuccess(registerBean: LoginRegisterResponse?) {
        // Toast.makeText(this, "注册成功😀", Toast.LENGTH_SHORT).show()
        registerModel ?.registerState ?.value = "恭喜 ${registerBean ?.username} 用户，注册成功"

        // TODO 注册成功，直接进入的登录界面  同学们去写
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun registerFailed(errorMsg: String?) {
        // Toast.makeText(this, "注册失败 ~ 呜呜呜", Toast.LENGTH_SHORT).show()
        registerModel ?.registerState ?.value = "骚瑞 注册失败，错误信息是:${errorMsg}"
    }

    inner class InnerClick {
        fun registerAction() {
            requestRegisterViewModel!!.requestRegister(this@RegisterActivity,
                registerModel!!.userName.value !!,
                registerModel!!.userPwd.value !!,
                registerModel!!.userPwd.value !!)
        }
    }

}