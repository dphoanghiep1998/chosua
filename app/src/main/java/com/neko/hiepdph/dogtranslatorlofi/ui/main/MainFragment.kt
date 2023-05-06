package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gianghv.libads.InterstitialSingleReqAdManager
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.InterAdsEnum
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.common.navigateToPage
import com.neko.hiepdph.dogtranslatorlofi.common.showInterAds
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    var lastClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        changeBackPressCallBack()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initButton()
    }

    private fun initButton() {
        binding.btnDog1.clickWithDebounce {
            navigateToPage(R.id.mainFragment,R.id.action_mainFragment_to_actionDogTypeFragment)
        }
        binding.btnDog2.clickWithDebounce {
            navigateToPage(R.id.mainFragment,R.id.action_mainFragment_to_roarDogTypeFragment)
        }
        binding.btnDogRecord.clickWithDebounce {
            navigateToPage(R.id.mainFragment,R.id.action_mainFragment_to_prankRecordFragment)
        }
        binding.btnDogShare.clickWithDebounce {
            shareApp()
        }
    }
    private fun changeBackPressCallBack() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 30000 && InterstitialSingleReqAdManager.isShowingAds) return
                    else showInterAds(action = {
//                        requireContext().pushEvent(BuildConfig.click_tool_wifidetector_back)
                        requireActivity().finishAffinity()
                    }, InterAdsEnum.BACK)
                    lastClickTime = SystemClock.elapsedRealtime()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }
    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=" + requireActivity().packageName
            )
            startActivity(Intent.createChooser(shareIntent, "Choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}