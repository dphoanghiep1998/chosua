package com.neko.hiepdph.dogtranslatorlofi.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.neko.hiepdph.dogtranslatorlofi.databinding.ActivityMainBinding
import com.neko.hiepdph.dogtranslatorlofi.viewmodel.AppViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<AppViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPlayer()
    }

    private fun initPlayer() {
        val player = ExoPlayer.Builder(this).build()

        viewModel.setupPlayer(player)
    }
}