package com.example.jetpatchlearning.player.helper

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaMetadataRetriever
import android.media.RemoteControlClient
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import com.example.jetpatchlearning.player.notification.PlayerReceiver

/**
 * 在来电时自动协调和暂停音乐播放
 * 只为 PlayerService 服务的
 */
class PlayerCallHelper(private val mPlayerCallHelperListener: PlayerCallHelperListener?) : OnAudioFocusChangeListener {

    private var phoneStateListener: PhoneStateListener? = null
    private var remoteControlClient: RemoteControlClient? = null
    private var mAudioManager: AudioManager? = null
    private var ignoreAudioFocus = false
    private var mIsTempPauseByPhone = false
    private var tempPause = false

    fun bindCallListener(context: Context) {
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, incomingNumber: String) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (mIsTempPauseByPhone) {
                        mPlayerCallHelperListener?.playAudio()
                        mIsTempPauseByPhone = false
                    }
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    if (mPlayerCallHelperListener != null) {
                        if (mPlayerCallHelperListener.isPlaying &&
                            !mPlayerCallHelperListener.isPaused
                        ) {
                            mPlayerCallHelperListener.pauseAudio()
                            mIsTempPauseByPhone = true
                        }
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                }
                super.onCallStateChanged(state, incomingNumber)
            }
        }
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun bindRemoteController(context: Context) {
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val remoteComponentName = ComponentName(context, PlayerReceiver::class.java.name)
        try {
            if (remoteControlClient == null) {
                mAudioManager!!.registerMediaButtonEventReceiver(remoteComponentName)
                val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                mediaButtonIntent.component = remoteComponentName
                val mediaPendingIntent = PendingIntent.getBroadcast(
                    context, 0, mediaButtonIntent, 0
                )
                remoteControlClient = RemoteControlClient(mediaPendingIntent)
                mAudioManager!!.registerRemoteControlClient(remoteControlClient)
            }
            remoteControlClient!!.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                        or RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                        or RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                        or RemoteControlClient.FLAG_KEY_MEDIA_STOP
                        or RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                        or RemoteControlClient.FLAG_KEY_MEDIA_NEXT
            )
        } catch (e: Exception) {
            Log.e("tmessages", e.toString())
        }
    }

    fun unbindCallListener(context: Context) {
        try {
            val mgr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            mgr?.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        } catch (e: Exception) {
            Log.e("tmessages", e.toString())
        }
    }

    fun unbindRemoteController() {
        if (remoteControlClient != null) {
            val metadataEditor = remoteControlClient!!.editMetadata(true)
            metadataEditor.clear()
            metadataEditor.apply()
            mAudioManager!!.unregisterRemoteControlClient(remoteControlClient)
            mAudioManager!!.abandonAudioFocus(this)
        }
    }

    fun requestAudioFocus(title: String?, summary: String?) {
        if (remoteControlClient != null) {
            val metadataEditor = remoteControlClient!!.editMetadata(true)
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, summary)
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title)
            metadataEditor.apply()
            mAudioManager!!.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    fun setIgnoreAudioFocus() {
        ignoreAudioFocus = true
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (ignoreAudioFocus) {
            ignoreAudioFocus = false
            return
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (mPlayerCallHelperListener != null) {
                if (mPlayerCallHelperListener.isPlaying &&
                    !mPlayerCallHelperListener.isPaused
                ) {
                    mPlayerCallHelperListener.pauseAudio()
                    tempPause = true
                }
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (tempPause) {
                mPlayerCallHelperListener?.playAudio()
                tempPause = false
            }
        }
    }

    interface PlayerCallHelperListener {
        fun playAudio()
        val isPlaying: Boolean
        val isPaused: Boolean
        fun pauseAudio()
    }
}