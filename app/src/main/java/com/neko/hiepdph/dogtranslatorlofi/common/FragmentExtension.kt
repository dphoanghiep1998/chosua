package com.neko.hiepdph.dogtranslatorlofi.common

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.gianghv.libads.AdaptiveBannerManager
import com.gianghv.libads.InterstitialSingleReqAdManager
import com.neko.hiepdph.dogtranslatorlofi.BuildConfig


 fun Fragment.navigateToPage(fragmentId: Int, actionId: Int, bundle: Bundle? = null) {
    if (fragmentId == findNavController().currentDestination?.id) {
        lifecycleScope.launchWhenResumed {
            findNavController().navigate(actionId, bundle)
        }
    }
}

 fun Fragment.navigateBack() {
    lifecycleScope.launchWhenResumed {
        findNavController().popBackStack()
    }
}

fun Activity.showBannerAds(view: ViewGroup, action: (() -> Unit)? = null) {
    val adaptiveBannerManager = AdaptiveBannerManager(
        this,
        BuildConfig.banner_home_id,
        BuildConfig.banner_home_id2,
        BuildConfig.banner_home_id3,
    )
    if (AdaptiveBannerManager.isBannerLoaded) {
        adaptiveBannerManager.loadAdViewToParent(view)
        return
    }

    adaptiveBannerManager.loadBanner(view,
        onAdLoadFail = { action?.invoke() },
        onAdLoader = { action?.invoke() })
}


fun Fragment.showInterAds(
    action: () -> Unit, type: InterAdsEnum
) {
    if (!isAdded) {
        action.invoke()
        return
    }
    if (!isInternetAvailable(requireContext())) {
        action.invoke()
        return
    }
    val interstitialSingleReqAdManager: InterstitialSingleReqAdManager
    when (type) {
        InterAdsEnum.SPLASH -> {
            interstitialSingleReqAdManager = InterstitialSingleReqAdManager(
                requireActivity(),
                BuildConfig.inter_splash_id,
                BuildConfig.inter_splash_id2,
                BuildConfig.inter_splash_id3,
            )
        }
        InterAdsEnum.BACK -> {
            interstitialSingleReqAdManager = InterstitialSingleReqAdManager(
                requireActivity(),
                BuildConfig.inter_back_id,
                BuildConfig.inter_back_id2,
                BuildConfig.inter_back_id3,
            )
        }
        InterAdsEnum.FUNCTION -> {
            interstitialSingleReqAdManager = InterstitialSingleReqAdManager(
                requireActivity(),
                BuildConfig.inter_function_id,
                BuildConfig.inter_function_id2,
                BuildConfig.inter_function_id3,
            )
        }


    }
    val dialogLoadingInterAds = DialogFragmentLoadingInterAds()
    dialogLoadingInterAds.show(childFragmentManager,dialogLoadingInterAds.tag)
    InterstitialSingleReqAdManager.isShowingAds = true

    interstitialSingleReqAdManager.showAds(requireActivity(), onLoadAdSuccess = {
        dialogLoadingInterAds.dismissAllowingStateLoss()
    }, onAdClose = {
        InterstitialSingleReqAdManager.isShowingAds = false
        action()
        dialogLoadingInterAds.dismissAllowingStateLoss()
    }, onAdLoadFail = {
        InterstitialSingleReqAdManager.isShowingAds = false
        action()
        dialogLoadingInterAds.dismissAllowingStateLoss()
    })

}

fun isInternetAvailable(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    if (netInfo != null) {
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    return false

}
