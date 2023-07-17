package com.example.jetpatchlearning.ui.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.databinding.FragmentPlayerBinding
import com.example.jetpatchlearning.player.PlayerManager
import com.example.jetpatchlearning.ui.base.BaseFragment
import com.kunminx.player.PlayingInfoManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.xiangxue.puremusic.ui.view.PlayerSlideListener
import net.steamcrafted.materialiconlib.MaterialDrawableBuilder

/**
 * 底部播放条的 Fragment
 */
class PlayerFragment : BaseFragment() {

    private var binding: FragmentPlayerBinding? = null

    private var playerViewModel: PlayerViewModel? = null

    // 播放不需要请求数据，不会有单独的 RequestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = getFragmentViewModelProvider(this).get<PlayerViewModel>(PlayerViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        // 加载界面
        val view: View = inflater.inflate(R.layout.fragment_player, container, false)
        // 绑定 Binding
        binding = FragmentPlayerBinding.bind(view)
        binding ?.click = ClickProxy()
        binding ?.event = EventHandler()
        binding ?.vm = playerViewModel
        return view
    }

    // 观察到数据的变化，我就变化
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 观察
        sharedViewModel.timeToAddSlideListener.observe(viewLifecycleOwner) {
            if (view.parent.parent is SlidingUpPanelLayout) {
                val sliding = view.parent.parent as SlidingUpPanelLayout

                // 添加监听（弹上来）
                sliding.addPanelSlideListener(PlayerSlideListener(binding!!, sliding))
            }
        }

        // 我是播放条，我要去变化，我成为观察者 -----> 播放相关的类PlayerManager
        PlayerManager.instance.changeMusicLiveData.observe(viewLifecycleOwner) { changeMusic ->

            // 例如 ：理解 切歌的时候， 音乐的标题，作者，封面 状态等 改变
            playerViewModel!!.title.set(changeMusic.title)
            playerViewModel!!.artist.set(changeMusic.summary)
            playerViewModel!!.coverImg.set(changeMusic.img)
        }

        // 我是播放条，我要去变化，我成为观察者 -----> 播放相关的类PlayerManager
        PlayerManager.instance.playingMusicLiveData.observe(viewLifecycleOwner) { playingMusic ->

            // 例如 ：理解 切歌的时候，  播放进度的改变  按钮图标的改变
            playerViewModel!!.maxSeekDuration.set(playingMusic.duration) // 总时长
            playerViewModel!!.currentSeekPosition.set(playingMusic.playerPosition) // 拖动条
        }

        // 播放/暂停是一个控件  图标的true和false
        PlayerManager.instance.pauseLiveData.observe(viewLifecycleOwner) { aBoolean ->
            playerViewModel!!.isPlaying.set(!aBoolean!!) // 播放时显示暂停，暂停时显示播放
        }

        // 列表循环，单曲循环，随机播放
        PlayerManager.instance.playModeLiveData.observe(viewLifecycleOwner) { anEnum ->
            val resultID: Int = if (anEnum === PlayingInfoManager.RepeatMode.LIST_LOOP) { // 列表循环
                playerViewModel!!.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT)
                R.string.play_repeat // 列表循环
            } else if (anEnum === PlayingInfoManager.RepeatMode.ONE_LOOP) { // 单曲循环
                playerViewModel!!.playModeIcon.set(MaterialDrawableBuilder.IconValue.REPEAT_ONCE)
                R.string.play_repeat_once // 单曲循环
            } else { // 随机循环
                playerViewModel!!.playModeIcon.set(MaterialDrawableBuilder.IconValue.SHUFFLE)
                R.string.play_shuffle // 随机循环
            }

            // 真正的改变
            if (view.parent.parent is SlidingUpPanelLayout) {
                val sliding = view.parent.parent as SlidingUpPanelLayout
                if (sliding.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    // 这里一定会弹出：“列表循环” or “单曲循环” or “随机播放”
                    showShortToast(resultID)
                }
            }
        }

        // 例如：场景  back  要不要做什么事情
        sharedViewModel.closeSlidePanelIfExpanded.observe(viewLifecycleOwner) {
            if (view.parent.parent is SlidingUpPanelLayout) {
                val sliding = view.parent.parent as SlidingUpPanelLayout

                // 如果是扩大，也就是，详情页面展示出来的
                if (sliding.panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    sliding.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED // 缩小了
                } else {
                    sharedViewModel.activityCanBeClosedDirectly.setValue(true)
                }
            } else {
                sharedViewModel.activityCanBeClosedDirectly.setValue(true)
            }
        }
    }

    inner class ClickProxy {

        /*public void playerMode() {
            PlayerManager.getInstance().changeMode();
        }*/

        fun previous() = PlayerManager.instance.playPrevious()

        operator fun next() = PlayerManager.instance.playNext()

        // 点击缩小的
        fun slideDown() = sharedViewModel.closeSlidePanelIfExpanded.setValue(true)

        //　更多的
        fun more() {}

        fun togglePlay() = PlayerManager.instance.togglePlay()

        fun playMode() = PlayerManager.instance.changeMode()

        fun showPlayList() = showShortToast("最近播放的细节，我没有搞...")
    }

    /**
     * 专门更新 拖动条进度相关的
     */
    inner class EventHandler : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
        override fun onStartTrackingTouch(seekBar: SeekBar) {}

        // 一拖动 松开手  把当前进度值 告诉PlayerManager
        override fun onStopTrackingTouch(seekBar: SeekBar) = PlayerManager.instance.setSeek(seekBar.progress)
    }

}