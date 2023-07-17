package com.example.jetpatchlearning.player.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.view.KeyEvent
import com.example.jetpatchlearning.player.PlayerManager
import com.xiangxue.puremusic.player.notification.PlayerService
import java.util.*

/**
 * 播放的广播
 * 用于接收 某些改变（系统发出来的信息，断网了），对音乐做出对应操作
 */
class PlayerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_MEDIA_BUTTON) {
            if (intent.extras == null) {
                return
            }

            val keyEvent = intent.extras!![Intent.EXTRA_KEY_EVENT] as KeyEvent? ?: return

            if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                return
            }

            when (keyEvent.keyCode) {
                KeyEvent.KEYCODE_HEADSETHOOK, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> PlayerManager.instance
                    .togglePlay()
                KeyEvent.KEYCODE_MEDIA_PLAY -> PlayerManager.instance.playAudio()
                KeyEvent.KEYCODE_MEDIA_PAUSE -> PlayerManager.instance.pauseAudio()
                KeyEvent.KEYCODE_MEDIA_STOP -> PlayerManager.instance.clear()
                KeyEvent.KEYCODE_MEDIA_NEXT -> PlayerManager.instance.playNext()
                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> PlayerManager.instance.playPrevious()
                else -> {
                }
            }
        } else {
            if (Objects.requireNonNull(intent.action) == PlayerService.NOTIFY_PLAY) {
                PlayerManager.instance.playAudio()
            } else if (intent.action == PlayerService.NOTIFY_PAUSE || intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                PlayerManager.instance.pauseAudio()
            } else if (intent.action == PlayerService.NOTIFY_NEXT) {
                PlayerManager.instance.playNext()
            } else if (intent.action == PlayerService.NOTIFY_CLOSE) {
                PlayerManager.instance.clear()
            } else if (intent.action == PlayerService.NOTIFY_PREVIOUS) {
                PlayerManager.instance.playPrevious()
            }
        }
    }
}