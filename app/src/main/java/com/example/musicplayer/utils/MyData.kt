package com.example.musicplayer.utils

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.models.Music

object MyData {

    var musicPosition = MutableLiveData<String>()
    var p: Int? = null
    var music: Music? = null
    var mediaPlayer: MediaPlayer? = null
    var list = ArrayList<Music>()
}