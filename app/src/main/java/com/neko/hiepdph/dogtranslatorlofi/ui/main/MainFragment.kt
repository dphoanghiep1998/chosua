package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
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
            findNavController().navigate(R.id.action_mainFragment_to_actionDogTypeFragment)
        }
        binding.btnDog2.clickWithDebounce {
            findNavController().navigate(R.id.action_mainFragment_to_roarDogTypeFragment)
        }
        binding.btnDogRecord.clickWithDebounce {
            findNavController().navigate(R.id.action_mainFragment_to_prankRecordFragment)
        }
    }

}