package com.example.jetpatchlearning.ui.page

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.bridge.request.MusicRequestViewModel
import com.example.jetpatchlearning.databinding.FragmentMainBinding
import com.example.jetpatchlearning.ui.base.BaseFragment
import com.example.jetpatchlearning.bridge.state.MainViewModel
import com.example.jetpatchlearning.data.bean.TestAlbum
import com.example.jetpatchlearning.databinding.AdapterPlayItemBinding
import com.example.jetpatchlearning.player.PlayerManager
import com.example.mylibrary.ui.adapter.SimpleBaseBindingAdapter

class MainFragment : BaseFragment() {
    // 我们操作布局，不去传统方式操作，全部使用Databindxxx
    private var mainBinding: FragmentMainBinding? = null
    private var mainViewModel : MainViewModel? = null // 首页Fragment的ViewModel   todo Status ViewModel

    // 适配器
    private var adapter: SimpleBaseBindingAdapter<TestAlbum.TestMusic?, AdapterPlayItemBinding?>? = null

    private var musicRequestViewModel: MusicRequestViewModel? = null // 音乐资源相关的VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = getFragmentViewModelProvider(this).get(MainViewModel::class.java)
        musicRequestViewModel = getFragmentViewModelProvider(this).get(MusicRequestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_main, container, false)
        mainBinding = FragmentMainBinding.bind(view)
        mainBinding ?.click = ClickProxy() // 设置点击事件，布局就可以直接绑定
        mainBinding ?.setVm(mainViewModel) // 设置VM，就可以实时数据变化
        mainBinding ?.lifecycleOwner = this // 设置生命周期的所有者，就可以实时数据变化
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 触发  --->  xxxx
        mainViewModel!!.initTabAndPage.set(true)

        // 触发，---> 还要加载WebView
        mainViewModel!!.pageAssetPath.set("JetPack之 WorkManager.html")

        // 展示数据，适配器里面的的数据 展示出来
        // 设置设配器(item的布局 和 适配器的绑定)
        adapter = object : SimpleBaseBindingAdapter<TestAlbum.TestMusic?, AdapterPlayItemBinding?>(context, R.layout.adapter_play_item) {
            override fun onSimpleBindItem(
                binding: AdapterPlayItemBinding?,
                item: TestAlbum.TestMusic?,
                holder: RecyclerView.ViewHolder?) {

                binding ?.tvTitle ?.text = item ?.title // 标题
                binding ?.tvArtist ?.text = item ?.artist ?.name // 歌手 就是 艺术家
                Glide.with(binding ?.ivCover!!.context).load(item ?.coverImg).into(binding.ivCover) // 左右边的图片

                // 歌曲下标记录
                val currentIndex = PlayerManager.instance.albumIndex // 歌曲下标记录

                // 播放的标记
                binding.ivPlayStatus.setColor(
                    if (currentIndex == holder ?.adapterPosition) resources.getColor(R.color.colorAccent) else Color.TRANSPARENT
                ) // 播放的时候，右变状态图标就是红色， 如果对不上的时候，就是没有

                // 点击Item
                binding.root.setOnClickListener { v ->
                    Toast.makeText(mContext, "播放音乐", Toast.LENGTH_SHORT).show()
                    PlayerManager.instance.playAudio(holder !!.adapterPosition)
                }
            }
        }

        mainBinding !!.rv.adapter = adapter


        // 播放相关业务的数据（如果这个数据发生了改变，为了更好的体验） 眼睛 盯着
        PlayerManager.instance.changeMusicLiveData.observe(viewLifecycleOwner, {
            adapter ?.notifyDataSetChanged() // 刷新适配器
        })

        // 请求数据
        // 保证我们列表没有数据（music list）
        if (PlayerManager.instance.album == null) {
            musicRequestViewModel !!.requestFreeMusics()
        }

        // 眼睛 监听的变化，你只要敢变，UI就要变
        // 观察到了 observe
        // 音乐资源的 VM
        // 此处理解就是观察者， 有一双眼睛盯着看，getFreeMusicsLiveData变化了，如果变化就执行
        musicRequestViewModel !!.freeMusicsLiveData !!.observe(viewLifecycleOwner) { musicAlbum: TestAlbum? ->
            if (musicAlbum != null && musicAlbum.musics != null) {
                // 这里特殊：直接更新UI，越快越好
                adapter?.list = musicAlbum.musics // 数据加入适配器
                adapter?.notifyDataSetChanged()

                // 播放相关的业务需要这个数据
                if (PlayerManager.instance.album == null ||
                    PlayerManager.instance.album!!.albumId != musicAlbum.albumId
                ) {
                    PlayerManager.instance.loadAlbum(musicAlbum)
                }
            }
        }

        // 眼睛 观察 登录Session的变化，只要发生改变，就需要更新到界面ui中
        sharedViewModel.session.observe(viewLifecycleOwner, {
            Log.d("Derry", "登录Session的变化，只要发生改变，就需要更新到界面UI中")

            // 检验操作
            // ....

            // 更新UI，面向ViewModel实例修改，UI就变了
            mainViewModel ?.loginSessionInfo ?.set(if (it.isLogin) "登录成功，欢迎${it.loginRegisterResponse ?.username}来到此系统" else "你未登录")
        })
    }

    inner class ClickProxy {
        // 当在首页点击 “菜单” 的时候，直接导航到 ---> 菜单的Fragment界面
        fun openMenu() { sharedViewModel.openOrCloseDrawer.value = true } // 触发

        // 当在首页点击 “搜索图标” 的时候，直接导航到 ---> 搜索的Fragment界面
        fun search() = nav().navigate(R.id.action_mainFragment_to_searchFragment)
    }
}