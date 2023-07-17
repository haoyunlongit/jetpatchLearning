package com.example.jetpatchlearning.data.reposity

import androidx.lifecycle.MutableLiveData
import com.example.jetpatchlearning.data.bean.DownloadFile
import com.example.jetpatchlearning.data.bean.LibraryInfo
import com.example.jetpatchlearning.data.bean.TestAlbum

interface IRemoteRequest {

    fun getFreeMusic(liveData: MutableLiveData<TestAlbum>?)

    fun getLibraryInfo(liveData: MutableLiveData<List<LibraryInfo>>?)

    fun downloadFile(liveData: MutableLiveData<DownloadFile>?)

}