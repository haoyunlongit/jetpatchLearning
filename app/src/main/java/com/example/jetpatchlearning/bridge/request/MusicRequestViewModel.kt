package com.example.jetpatchlearning.bridge.request

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jetpatchlearning.data.bean.TestAlbum
import com.example.jetpatchlearning.data.reposity.HttpRequestManager

class MusicRequestViewModel : ViewModel() {
    var freeMusicsLiveData: MutableLiveData<TestAlbum>? = null
        get() {
            if (field == null) {
                field = MutableLiveData()
            }
            return field
        }
        private set

    fun requestFreeMusics() {
        HttpRequestManager.instance.getFreeMusic(freeMusicsLiveData)
    }
}