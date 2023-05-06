package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gianghv.libads.InterstitialSingleReqAdManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.InterAdsEnum
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.common.navigateBack
import com.neko.hiepdph.dogtranslatorlofi.common.showInterAds
import com.neko.hiepdph.dogtranslatorlofi.data.DogModel
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentActionDogTypeBinding
import com.neko.hiepdph.dogtranslatorlofi.viewmodel.AppViewModel

class ActionDogTypeFragment : Fragment() {

    private lateinit var binding: FragmentActionDogTypeBinding
    private var adapter: ActionDogTypeAdapter? = null
    private val viewModel by activityViewModels<AppViewModel>()
    var lastClickTime: Long = 0
    var handler = Handler()
    var runnable = Runnable {
        InterstitialSingleReqAdManager.isShowingAds = false
    }
    override fun onResume() {
        super.onResume()
        if (lastClickTime > 0) {
            handler.postDelayed(runnable, 1000)
        }
    }
    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentActionDogTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun changeBackPressCallBack() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 30000 && InterstitialSingleReqAdManager.isShowingAds) return
                    else showInterAds(action = {
//                        requireContext().pushEvent(BuildConfig.click_tool_wifidetector_back)
                        navigateBack()
                    }, InterAdsEnum.BACK)
                    lastClickTime = SystemClock.elapsedRealtime()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        changeBackPressCallBack()
    }

    private fun initView() {
        initRecyclerView()
        initButton()
    }

    private fun initButton() {
        binding.btnBack.clickWithDebounce {
            if (SystemClock.elapsedRealtime() - lastClickTime < 30000 && InterstitialSingleReqAdManager.isShowingAds) return@clickWithDebounce
            else showInterAds(action = {
//                requireContext().pushEvent(BuildConfig.click_tool_wifidetector_back)
                navigateBack()
            }, InterAdsEnum.BACK)
            lastClickTime = SystemClock.elapsedRealtime()
        }
    }

    private fun initRecyclerView() {
        val data = mutableListOf(
            DogModel(R.drawable.ic_dog_1_1, R.string.curious, R.raw.e),
            DogModel(R.drawable.ic_dog_1_2, R.string.suspicious, R.raw.i),
            DogModel(R.drawable.ic_dog_1_3, R.string.stalking, R.raw.o),
            DogModel(R.drawable.ic_dog_1_4, R.string.guilty, R.raw.q),
            DogModel(R.drawable.ic_dog_1_5, R.string.let_play, R.raw.chosuadefault),
            DogModel(R.drawable.ic_dog_1_6, R.string.angry, R.raw.r),
            DogModel(R.drawable.ic_dog_1_7, R.string.friendly, R.raw.t),
            DogModel(R.drawable.ic_dog_1_8, R.string.requires, R.raw.u),
            DogModel(R.drawable.ic_dog_1_9, R.string.scared, R.raw.w),
            DogModel(R.drawable.ic_dog_1_10, R.string.happy, R.raw.y),
            DogModel(R.drawable.ic_dog_1_11, R.string.tired, R.raw.e),
            DogModel(R.drawable.ic_dog_1_12, R.string.trusts, R.raw.e),
        )
        adapter = ActionDogTypeAdapter {
            val mediaItem =
                MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(it.audio))
            viewModel.playAudio(mediaItem, onEnd = {
                adapter?.clearSection()
                showInterAds({},InterAdsEnum.FUNCTION)
            })
        }
        binding.rcvDog.adapter = adapter
        val layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rcvDog.layoutManager = layoutManager
        adapter?.setData(data)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.clearSection()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetPlayer()
    }

}