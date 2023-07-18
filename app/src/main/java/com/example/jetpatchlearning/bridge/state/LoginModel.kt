package com.example.jetpatchlearning.bridge.state
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginModel : ViewModel() {

    @JvmField // @JvmField消除了变量的getter方法
    val userName = MutableLiveData<String>()
    val userPwd = MutableLiveData<String>()
    val loginState = MutableLiveData<String>()

    init {
        userName.value = ""
        userPwd.value = ""
        loginState.value = ""
    }

}