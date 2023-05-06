package com.neko.hiepdph.dogtranslatorlofi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class AppViewModel : ViewModel() {
    private var _player: Player? = null
    private var playerListener: Player.Listener? = null

    fun setupPlayer(player: Player) {
        if (_player == null) {
            _player = player

        }
    }


    fun playAudio(mediaItem: MediaItem, onEnd: () -> Unit) {

        try {
            if (_player?.isPlaying == true || _player?.isLoading == true) {
                playerListener?.let { _player?.removeListener(it) }
                _player?.stop()
            }
             playerListener = object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if (playbackState == Player.STATE_ENDED) {
                        Log.d("TAG", "onPlaybackStateChanged: " + this)
                        onEnd()
                        _player?.removeListener(this)
                    }
                }
            }


            _player?.addListener(playerListener!!)
            _player?.clearMediaItems()
            _player?.setMediaItem(mediaItem)
            _player?.prepare()
            _player?.playWhenReady = true


        } catch (e: Exception) {
        }

    }


    fun playAudio(mediaItem: MutableList<MediaItem>, timeLimit: Long, onEnd: () -> Unit) {
        try {
            Log.d("TAG", "playAudio: " + timeLimit)
            var bindingThis: Player.Listener?
            val listener = object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    bindingThis = this
                    super.onIsPlayingChanged(isPlaying)
                    viewModelScope.launch {
                        delay(timeLimit)
                        _player?.let {
                            it.stop()
                            it.clearMediaItems()
                            _player?.playWhenReady = false
                            it.removeListener(bindingThis as Player.Listener)
                            onEnd()

                        }

                    }


                }
            }
            if (_player?.isPlaying == true || _player?.isLoading == true) {
                _player?.stop()
            }
            _player?.addListener(listener)
            _player?.clearMediaItems()
            _player?.repeatMode = Player.REPEAT_MODE_ALL
            _player?.addMediaItems(mediaItem)
            _player?.prepare()
            _player?.playWhenReady = true


        } catch (e: Exception) {
        }

    }

    fun resetPlayer() {
        if (_player?.isPlaying == true || _player?.isLoading == true) {
            _player?.stop()
        }
        _player?.clearMediaItems()
        _player?.apply {
            playWhenReady = false
            playbackState
        }
    }

    fun resetAll() {
        if (_player?.isPlaying == true || _player?.isLoading == true) {
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