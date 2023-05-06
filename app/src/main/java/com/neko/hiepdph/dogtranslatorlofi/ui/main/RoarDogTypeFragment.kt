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
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.InterAdsEnum
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.common.navigateBack
import com.neko.hiepdph.dogtranslatorlofi.common.showInterAds
import com.neko.hiepdph.dogtranslatorlofi.data.DogModel
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentRoarDogTypeBinding
import com.neko.hiepdph.dogtranslatorlofi.viewmodel.AppViewModel


class RoarDogTypeFragment : Fragment() {

    private lateinit var binding: FragmentRoarDogTypeBinding
    private val viewModel by activityViewModels<AppViewModel>()
    private var adapter: RoarDogTypeAdapter? = null
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
        binding = FragmentRoarDogTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        changeBackPressCallBack()
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

    private fun initView() {
        initButton()
        initRecyclerView()
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
        adapter = RoarDogTypeAdapter {
            val resources = requireContext().resources
            val rawResourceId = it.audio
            val uri = Uri.Builder().scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(rawResourceId))
                .appendPath(resources.getResourceTypeName(rawResourceId))
                .appendPath(resources.getResourceEntryName(rawResourceId)).build()

            val mediaItem = MediaItem.Builder().setUri(uri).build()
            viewModel.playAudio(mediaItem, onEnd = {
                Log.d("TAG", "initRecyclerView: ")
                adapter?.clearSection()
                showInterAds({},InterAdsEnum.FUNCTION)
            })
        }
        binding.rcvDog.adapter = adapter
        val gridLayoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.rcvDog.layoutManager = gridLayoutManager

        val data = mutableListOf(
            DogModel(R.drawable.ic_dog_2_1, R.string.curious, R.raw.e),
            DogModel(R.drawable.ic_dog_2_2, R.string.suspicious, R.raw.i),
            DogModel(R.drawable.ic_dog_2_3, R.string.stalking, R.raw.o),
            DogModel(R.drawable.ic_dog_2_4, R.string.guilty, R.raw.q),
            DogModel(R.drawable.ic_dog_2_5, R.string.let_play, R.raw.chosuadefault),
            DogModel(R.drawable.ic_dog_2_6, R.string.angry, R.raw.r),
            DogModel(R.drawable.ic_dog_2_7, R.string.friendly, R.raw.t),
            DogModel(R.drawable.ic_dog_2_8, R.string.requires, R.raw.u),
            DogModel(R.drawable.ic_dog_2_9, R.string.scared, R.raw.w),
            DogModel(R.drawable.ic_dog_2_10, R.string.happy, R.raw.y),
            DogModel(R.drawable.ic_dog_2_11, R.string.tired, R.raw.e),
            DogModel(R.drawable.ic_dog_2_12, R.string.trusts, R.raw.e),
        )

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