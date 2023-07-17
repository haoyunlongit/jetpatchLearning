package com.example.jetpatchlearning.ui

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.jetpatchlearning.player.PlayerManager
import com.example.mylibrary.utils.Utils


/// ViewModelStoreOwner
class App : Application(), ViewModelStoreOwner {

    private var mAppViewModelStore: ViewModelStore? = null

    private var mFactory: ViewModelProvider.Factory? = null

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        mAppViewModelStore = ViewModelStore()

        PlayerManager.instance.init(this)
    }

    fun getAppViewModelProvider(activity: Activity): ViewModelProvider {
        return ViewModelProvider(
            (activity.applicationContext as App),
            (activity.applicationContext as App).getAppFactory(activity)
        )
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        }
        return mFactory!!
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore !!

}