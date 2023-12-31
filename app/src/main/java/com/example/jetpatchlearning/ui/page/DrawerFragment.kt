package com.example.jetpatchlearning.ui.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpatchlearning.R
import com.example.jetpatchlearning.bridge.request.InfoRequestViewModel
import com.example.jetpatchlearning.data.bean.LibraryInfo
import com.example.jetpatchlearning.databinding.AdapterLibraryBinding
import com.example.jetpatchlearning.databinding.FragmentDrawerBinding
import com.example.jetpatchlearning.ui.base.BaseFragment
import com.example.mylibrary.ui.adapter.SimpleBaseBindingAdapter

class DrawerFragment : BaseFragment() {
    private var mBinding: FragmentDrawerBinding? = null
    private var mDrawerViewModel: DrawerViewModel? = null

    private var infoRequestViewModel: InfoRequestViewModel? = null

    private var mAdapter: SimpleBaseBindingAdapter<LibraryInfo?, AdapterLibraryBinding?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDrawerViewModel = getFragmentViewModelProvider(this).get(DrawerViewModel::class.java)
        infoRequestViewModel = getFragmentViewModelProvider(this).get(InfoRequestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View { // DataBinding 与 ViewModel关联
        val view: View = inflater.inflate(R.layout.fragment_drawer, container, false)
        mBinding = FragmentDrawerBinding.bind(view)
        mBinding ?.vm = mDrawerViewModel
        mBinding ?.click = ClickProxy() // 点击
        return view
    }

    // 获取 Item数据集合
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = object : SimpleBaseBindingAdapter<LibraryInfo?, AdapterLibraryBinding?>(
            context,
            R.layout.adapter_library
        ) {

            override fun onSimpleBindItem(
                binding: AdapterLibraryBinding?,
                item: LibraryInfo?,
                holder: RecyclerView.ViewHolder?
            ) {

                // 把数据 设置好，就显示数据了
                binding?.info = item
                binding?.root?.setOnClickListener {
                    Toast.makeText(mContext, "哎呀，还在研发中，猴急啥?...", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 设置适配器 到 RecyclerView
        mBinding!!.rv.adapter = mAdapter

        // 观察者 监听 眼睛 数据发送改变，UI就马上刷新
        // 观察这个数据的变化，如果 libraryLiveData 变化了，我就要要变，我就要更新到 RecyclerView
        infoRequestViewModel!!.libraryInfoLiveData ?.observe(viewLifecycleOwner, { libraryInfos ->

            // 以前是 间接的通过 状态VM 修改
            // mDrawerViewModel.xxx.setValue = libraryInfos

            // 这里特殊：直接更新UI，越快越好
            mAdapter ?.list = libraryInfos
            mAdapter ?.notifyDataSetChanged()
        })

        // 请求数据 调用 Request 的 ViewModel 加载数据
        infoRequestViewModel!!.requestLibraryInfo()
    }

    inner class ClickProxy {
        fun logoClick() = Toast.makeText(mActivity, "哎呀，你能不能不要乱点啊，程序员还在玩命编码中...", Toast.LENGTH_SHORT).show()
    }
}