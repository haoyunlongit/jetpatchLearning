package com.example.jetpatchlearning.bridge.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    // 首页需要记录抽屉布局的情况 （响应的效果，都让 抽屉控件干了）
    @JvmField
    val openDrawer = MutableLiveData<Boolean>()

    // （响应的效果，都让 抽屉控件干了）
    @JvmField
    val allowDrawerOpen = MutableLiveData<Boolean>()

    // 构造代码块
    init {
        allowDrawerOpen.value = true
    }
}