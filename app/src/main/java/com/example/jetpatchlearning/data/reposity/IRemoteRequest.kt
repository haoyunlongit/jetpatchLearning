package com.example.jetpatchlearning.data.reposity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.jetpatchlearning.data.bean.DownloadFile
import com.example.jetpatchlearning.data.bean.LibraryInfo
import com.example.jetpatchlearning.data.bean.TestAlbum
import com.example.jetpatchlearning.data.login_register.LoginRegisterResponse

interface IRemoteRequest {

    fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?)

    fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>?)

    fun downloadFile(liveData: MutableLiveData<DownloadFile>?)

    // 注册
    // 注册的标准
    fun register(
        context: Context,
        username: String,
        password: String,
        repassword: String,
        dataLiveData1: MutableLiveData<LoginRegisterResponse>,
        dataLiveData2: MutableLiveData<String>)

    // 登录
    // 登录的标准
    fun login(
        context: Context,
        username: String,
        password: String,
        dataLiveData1: MutableLiveData<LoginRegisterResponse>,
        dataLiveData2: MutableLiveData<String>)


    // 登录的标准-协程版本
    suspend fun loginCoroutine(
        username: String,
        password: String)
            : LoginRegisterResponse

}