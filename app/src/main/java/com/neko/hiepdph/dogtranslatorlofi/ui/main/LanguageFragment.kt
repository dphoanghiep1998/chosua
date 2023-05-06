package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gianghv.libads.NativeAdsManager
import com.google.android.gms.ads.nativead.NativeAd
import com.neko.hiepdph.dogtranslatorlofi.BuildConfig
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.*
import com.neko.hiepdph.dogtranslatorlofi.common.AppSharePreference.Companion.INSTANCE
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentLanguageBinding
import com.neko.hiepdph.dogtranslatorlofi.ui.CustomApplication
import java.util.*

class LanguageFragment : Fragment() {
    private lateinit var binding: FragmentLanguageBinding
    private var currentLanguage = Locale.getDefault().language

    private var adapter: AdapterLanguage? = null
    private var nativeAd: NativeAd? = null
    private val initDone = INSTANCE.getSetLangFirst(false)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(initDone){
            navigateToPage(R.id.languageFragment,R.id.action_languageFragment_to_mainFragment)
        }
        initView()
    }

    private fun initView() {
        initRecyclerView()
        initButton()
    }

    private fun initButton() {
        binding.btnDone.clickWithDebounce {
            startActivity(requireActivity().intent)
            requireActivity().finish()
            INSTANCE.saveIsSetLangFirst(true)
            INSTANCE.saveLanguage(currentLanguage)
        }
    }

    private fun initRecyclerView() {
        val mLanguageList: MutableList<Any> = supportedLanguages().toMutableList()
        val mDisplayLangList: MutableList<Any> = supportDisplayLang().toMutableList()
        handleUnSupportLang(mLanguageList)
        mLanguageList.add(1, "adsApp")
        mDisplayLangList.add(1, "adsApp")
        adapter = AdapterLanguage(requireContext(), onCLickItem = {

        })
        adapter?.setData(mLanguageList,mDisplayLangList)
        binding.rcvLanguage.adapter = adapter
        binding.rcvLanguage.layoutManager = LinearLayoutManager(requireContext())
        adapter?.setCurrentLanguage(getCurrentLanguage())


        if (CustomApplication.app.nativeAD == null) {
            loadAds()
        } else {
            adapter?.insertAds(CustomApplication.app.nativeAD!!)
        }

    }

    private fun handleUnSupportLang(mLanguageList: MutableList<Any>) {
        var support = false
        mLanguageList.forEachIndexed { index, item ->
            if (item is Locale) {
                if (item.language == currentLanguage) {
                    support = true
                }
            }
        }
        if (!support) {
            currentLanguage = (mLanguageList[0] as Locale).language
        }
    }

    private fun getCurrentLanguage(): String {
        return INSTANCE.getSavedLanguage(Locale.getDefault().language)
    }

    private fun loadAds() {
        val mNativeAdManager = NativeAdsManager(
            requireContext(),
            BuildConfig.native_language_id,
            BuildConfig.native_language_id2,
            BuildConfig.native_language_id3,
        )
        mNativeAdManager.loadAds(onLoadSuccess = {
            nativeAd = it
            adapter?.insertAds(it)

        }, onLoadFail = {})

    }

}