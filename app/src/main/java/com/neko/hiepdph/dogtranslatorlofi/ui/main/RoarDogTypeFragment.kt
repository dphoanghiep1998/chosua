package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.MediaItem
import com.neko.hiepdph.dogtranslatorlofi.R
import com.neko.hiepdph.dogtranslatorlofi.common.clickWithDebounce
import com.neko.hiepdph.dogtranslatorlofi.data.DogModel
import com.neko.hiepdph.dogtranslatorlofi.databinding.FragmentRoarDogTypeBinding
import com.neko.hiepdph.dogtranslatorlofi.viewmodel.AppViewModel


class RoarDogTypeFragment : Fragment() {

    private lateinit var binding: FragmentRoarDogTypeBinding
    private val viewModel by activityViewModels<AppViewModel>()
    private var adapter: RoarDogTypeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoarDogTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initButton()
        initRecyclerView()
    }

    private fun initButton() {
        binding.btnBack.clickWithDebounce {
            findNavController().popBackStack()
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
            viewModel.playAudio(mediaItem)
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

}