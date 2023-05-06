package com.neko.hiepdph.dogtranslatorlofi.ui.splash

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ProgressBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.gianghv.libads.InterstitialPreloadAdManager
import com.gianghv.libads.InterstitialSingleReqAdManager
import com.gianghv.libads.NativeAdsManager
import com.neko.hiepdph.dogtranslatorlofi.BuildConfig
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.*
import com.neko.hiepdph.dogtranslatorlofi.databinding.ActivitySplashBinding
import com.neko.hiepdph.dogtranslatorlofi.ui.CustomApplication
import com.neko.hiepdph.dogtranslatorlofi.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private var interSplashAds: InterstitialPreloadAdManager? = null
    private var mNativeAdManager: NativeAdsManager? = null
    private var status = MutableLiveData(0)
    private val initDone = AppSharePreference.INSTANCE.getSetLangFirst(false)
    private var dialogLoadingInterAds: DialogFragmentLoadingInterAds? = null
    private lateinit var app: CustomApplication


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as CustomApplication
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialogLoadingInterAds = DialogFragmentLoadingInterAds()
        initAds()
        observeStatusLoadingAds()
        handleAds()
        changeStatusBarColor()
    }

    private fun initAds() {
        interSplashAds = InterstitialPreloadAdManager(
            this,
            BuildConfig.inter_splash_id,
            BuildConfig.inter_splash_id2,
            BuildConfig.inter_splash_id3,
        )

        mNativeAdManager = NativeAdsManager(
            this,
            BuildConfig.native_language_id,
            BuildConfig.native_language_id2,
            BuildConfig.native_language_id3,
        )


    }

    private fun observeStatusLoadingAds() {
        val callback = object : InterstitialPreloadAdManager.InterstitialAdListener {
            override fun onClose() {
                dialogLoadingInterAds?.dismissAllowingStateLoss()
            }

            override fun onError() {
                dialogLoadingInterAds?.dismissAllowingStateLoss()
            }
        }
        status.observe(this) {
            if (it >= 2) {
                if (interSplashAds?.loadAdsSuccess == true) {
                    handleAtLeast3Second(action = {
                        dialogLoadingInterAds?.show(supportFragmentManager,dialogLoadingInterAds?.tag)
                        lifecycleScope.launch {
                            delay(500)
                            navigateMain()
                            interSplashAds?.showAds(
                                this@SplashActivity, callback
                            )
                        }
                    })
                } else {
                    navigateMain()
                }

            } else if (initDone && it >= 1) {
                if (interSplashAds?.loadAdsSuccess == true) {
                    handleAtLeast3Second(action = {
                        dialogLoadingInterAds?.show(supportFragmentManager,dialogLoadingInterAds?.tag)
                        lifecycleScope.launch {
                            delay(500)
                            navigateMain()
                            interSplashAds?.showAds(
                                this@SplashActivity, callback
                            )
                        }
                    })
                } else {
                    navigateMain()
                }
            }
        }
    }

    private fun handleAds() {
        if (!isInternetAvailable(this)) {
            handleWhenAnimationDone(action = {
                navigateMain()
            })
        } else {
            if (initDone) {
                Handler().postDelayed({ loadSplashAds() }, 1000)
            } else {
                Handler().postDelayed({
                    loadNativeAds()
                    loadSplashAds()
                }, 1000)
            }
        }
    }

    private fun loadNativeAds() {
        mNativeAdManager?.loadAds(onLoadSuccess = {
            app.nativeAD = it
            status.postValue(status.value?.plus(1) ?: 0)
        }, onLoadFail = {
            Log.d("TAG", "loadNativeAdsFail: ")
            status.postValue(status.value?.plus(1) ?: 0)
        })
    }

    private fun loadSplashAds() {
        interSplashAds?.loadAds(onAdLoadFail = {
            status.postValue(status.value?.plus(1) ?: 0)
            interSplashAds?.loadAdsSuccess = false
            Log.d("TAG", "loadSplashAdsFail: ")

        }, onAdLoader = {
            status.postValue(status.value?.plus(1) ?: 0)
            interSplashAds?.loadAdsSuccess = true
        })
    }

    private fun handleWhenAnimationDone(action: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main){
            delay(3000)
            action.invoke()
        }

    }

    private fun handleAtLeast3Second(action: () -> Unit) {
        lifecycleScope.launch(Dispatchers.Main){
            delay(1000)
            action.invoke()
        }
    }

    private fun navigateMain() {
        val i = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        InterstitialPreloadAdManager.isShowingAds = false
        InterstitialSingleReqAdManager.isShowingAds = false
//        dialogLoadingInterAds?.dismissAllowingStateLoss()

    }
}