package com.example.jetpatchlearning.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpatchlearning.data.bean.LibraryInfo
import com.example.jetpatchlearning.data.reposity.HttpRequestManager

class InfoRequestViewModel : ViewModel() {
    var libraryInfoLiveData: MutableLiveData<List<LibraryInfo>>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }


    fun requestLibraryInfo() {
        HttpRequestManager.instance.getLibraryInfo(libraryInfoLiveData)
    }
}