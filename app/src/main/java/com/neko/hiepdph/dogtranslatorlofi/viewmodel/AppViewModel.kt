package com.neko.hiepdph.dogtranslatorlofi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class AppViewModel : ViewModel() {
    private var _player: Player? = null

    fun setupPlayer(player: Player) {
        if (_player == null) {
            _player = player

        }
    }

    fun playAudio(mediaItem: MediaItem) {
        try {
            if (_player?.isPlaying == true || _player?.isLoading == true) {
                _player?.stop()
            }
            _player?.clearMediaItems()
            _player?.setMediaItem(mediaItem)
            _player?.prepare()
            _player?.playWhenReady = true


        } catch (e: Exception) {
        }

    }


    fun playAudio(mediaItem: MutableList<MediaItem>, timeLimit: Long) {
        try {
            Log.d("TAG", "playAudio: "+timeLimit)
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    Log.d("TAG", "onIsPlayingChanged: "+_player?.currentPosition)
                    _player?.let {
                        if (it.currentPosition > timeLimit) {
                            it.stop()
                            it.clearMediaItems()
                            _player?.playWhenReady = false
                            it.removeListener(this)
                        }
                    }

                }
            }
            if (_player?.isPlaying == true || _player?.isLoading == true) {
                _player?.stop()
            }
            _player?.addListener(listener)
            _player?.clearMediaItems()
            _player?.addMediaItems(mediaItem)
            _player?.prepare()
            _player?.playWhenReady = true


        } catch (e: Exception) {
        }

    }

    fun resetAll() {
        if (_player?.isPlaying == true) {
            _player?.stop()
        }
        _player?.apply {
            playWhenReady = false
            playbackState
        }
        _player?.release()
        _player = null

    }
}