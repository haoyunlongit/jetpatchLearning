package com.example.jetpatchlearning.bridge.state

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpatchlearning.data.reposity.HttpRequestManager

class RegisterModel: ViewModel() {

    var registerState: MutableLiveData<String> = MutableLiveData()

    var userName: MutableLiveData<String> = MutableLiveData()

    var userPwd: MutableLiveData<String> = MutableLiveData()

}