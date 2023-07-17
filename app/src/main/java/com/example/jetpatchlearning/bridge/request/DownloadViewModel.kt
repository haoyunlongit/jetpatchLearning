package com.example.jetpatchlearning.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpatchlearning.data.bean.DownloadFile
import com.example.jetpatchlearning.data.reposity.HttpRequestManager

class DownloadViewModel : ViewModel() {

    var downloadFileLiveData: MutableLiveData<DownloadFile>? = null
        get() {
            if (field == null) {
                field = MutableLiveData<DownloadFile>()
            }
            return field
        }
        private set

    fun requestDownload() {
         HttpRequestManager.instance.downloadFile(downloadFileLiveData)
    }
}
