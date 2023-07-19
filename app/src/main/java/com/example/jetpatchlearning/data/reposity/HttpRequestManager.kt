package com.example.jetpatchlearning.data.reposity

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.data.bean.DownloadFile
import com.example.jetpatchlearning.data.bean.LibraryInfo
import com.example.jetpatchlearning.data.bean.TestAlbum
import com.example.jetpatchlearning.data.login_register.LoginRegisterResponse
import com.example.jetpatchlearning.data.repository.api.WanAndroidApi
import com.example.jetpatchlearning.data.repository.net.APIClient
import com.example.jetpatchlearning.data.repository.net.APIResponse
import com.example.mylibrary.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class HttpRequestManager private constructor() : IRemoteRequest, ILoadRequest {
    companion object {
        val instance: HttpRequestManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HttpRequestManager()
        }
    }

    override fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?) {
        val gson = Gson()
        val type = object : TypeToken<TestAlbum?>() {}.type
        val testAlbum =
            gson.fromJson<TestAlbum>(Utils.getApp().getString(R.string.free_music_json), type)

        // TODO 在这里可以请求网络
        // TODO 在这里可以请求网络
        // TODO 在这里可以请求数据库
        // .....

        liveData!!.value = testAlbum
    }

    override fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>?) {
        val gson = Gson()
        val type = object : TypeToken<List<LibraryInfo?>?>() {}.type
        val list =
            gson.fromJson<List<LibraryInfo?>>(Utils.getApp().getString(R.string.library_json), type)
        liveData!!.value = list as List<LibraryInfo>?
    }

    override fun downloadFile(liveData: MutableLiveData<DownloadFile>?) {
        val timer = Timer()
        val task: TimerTask = object : TimerTask() {
            override fun run() {

                //模拟下载，假设下载一个文件要 10秒、每 100 毫秒下载 1% 并通知 UI 层
                var downloadFile = liveData!!.value
                if (downloadFile == null) {
                    downloadFile = DownloadFile()
                }
                if (downloadFile.progress < 100) {
                    downloadFile.progress = downloadFile.progress + 1
                    Log.d("TAG", "下载进度 " + downloadFile.progress + "%")
                } else {
                    timer.cancel()
                    downloadFile.progress = 0
                    return
                }
                if (downloadFile.isForgive) {
                    timer.cancel()
                    downloadFile.progress = 0
                    return
                }
                liveData.postValue(downloadFile)
                downloadFile(liveData)
            }
        }
        timer.schedule(task, 100)
    }

    override fun register(
        context: Context,
        username: String,
        password: String,
        repassword: String,
        dataLiveData1: MutableLiveData<LoginRegisterResponse>,
        dataLiveData2: MutableLiveData<String>
    ) {
       APIClient.instance.instanceRetrofit(WanAndroidApi::class.java)
           .registerAction(username, password, repassword)
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(object : APIResponse<LoginRegisterResponse>(context) {
                override fun success(data: LoginRegisterResponse?) {
                     dataLiveData1.value = data
                }

                override fun failure(errorMsg: String?) {
                     dataLiveData2.value = errorMsg
                }
              })

    }

    override fun login(
        context: Context,
        username: String,
        password: String,
        dataLiveData1: MutableLiveData<LoginRegisterResponse>,
        dataLiveData2: MutableLiveData<String>
    ) {
        TODO("Not yet implemented")
    }

    // 登录的标准-协程版本-的具体代码
    override suspend fun loginCoroutine(
        username: String,
        password: String)
            =
        APIClient.instance.instanceRetrofit(WanAndroidApi::class.java)
            .loginActionCoroutine(username, password).data


}