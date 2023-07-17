package com.xiangxue.puremusic.data.config

import android.os.Environment
import com.example.mylibrary.utils.Utils

object Configs {

    // 封面路径地址
    @JvmField
    val COVER_PATH = Utils.getApp().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath

    const val TAG = "Derry"
}