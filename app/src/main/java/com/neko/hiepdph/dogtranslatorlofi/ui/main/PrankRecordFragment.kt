package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gianghv.libads.InterstitialSingleReqAdManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.*
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentPrankRecordBinding
import com.neko.hiepdph.dogtranslatorlofi.viewmodel.AppViewModel

class PrankRecordFragment : Fragment() {
    private lateinit var binding: FragmentPrankRecordBinding
    private val viewModel by activityViewModels<AppViewModel>()
    private var isRecording = false
    private val animSet = AnimationSet(true)
    private var currentTime = 0L
    private var timeRecord = 0L

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
        binding = FragmentPrankRecordBinding.inflate(inflater, container, false)
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
        val alphaIn = AlphaAnimation(0.0f, 1.0f)
        alphaIn.duration = 800
        animSet.addAnimation(alphaIn)

        val alphaOut = AlphaAnimation(1.0f, 0.0f)
        alphaOut.duration = 800
        animSet.addAnimation(alphaOut)
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
        binding.btnRecord.clickWithDebounce {
            if (!isRecording) {
                val anim = AlphaAnimation(
                    1.0f,
                    0.3f,
                )
                anim.duration = 600
                anim.repeatCount = Animation.INFINITE
                anim.repeatMode = Animation.REVERSE
                binding.recording.show()
                binding.btnRecord.startAnimation(anim)
                binding.btnPlay.clearAnimation()
                binding.btnPlay.isEnabled = false
                isRecording=true
                startFakeRecording()
            } else {
                binding.btnPlay.isEnabled = true
                endFakeRecording()
                isRecording = false
                binding.btnRecord.clearAnimation()
                binding.recording.hide()
                binding.playInstruction.show()
                binding.btnPlay.show()
            }
        }

        binding.btnPlay.clickWithDebounce {
            val anim = AlphaAnimation(
                1.0f,
                0.3f,
            )
            anim.duration = 600
            anim.repeatCount = Animation.INFINITE
            anim.repeatMode = Animation.REVERSE
            binding.recording.show()
            binding.btnPlay.startAnimation(anim)
            playTranslateRecording()
        }
    }

    private fun startFakeRecording() {
        currentTime = SystemClock.elapsedRealtime()
    }

    private fun endFakeRecording() {
        timeRecord = SystemClock.elapsedRealtime() - currentTime
    }

    private fun playTranslateRecording() {
        val data = mutableListOf(
            R.raw.e,
            R.raw.i,
            R.raw.o,
            R.raw.q,
            R.raw.chosuadefault,
            R.raw.r,
            R.raw.t,
            R.raw.u,
            R.raw.w,
            R.raw.y,
            R.raw.alaska,
            R.raw.begie,
            R.raw.chihuahua,
            R.raw.choconkeu,
            R.raw.chokinhhoang,
            R.raw.choconkhoc,
            R.raw.chogamgu,
            R.raw.chogaugau,
            R.raw.chooang,
            R.raw.phuquoc,
            R.raw.poodle,
            R.raw.pugmatxe,
        )
        data.shuffle()
        val listOfMediaItem = data.map {
            MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(it))
        }
        viewModel.playAudio(listOfMediaItem.toMutableList(),timeRecord,onEnd={
            binding.btnPlay.clearAnimation()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.btnPlay.clearAnimation()
        binding.btnRecord.clearAnimation()
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetPlayer()
    }

}