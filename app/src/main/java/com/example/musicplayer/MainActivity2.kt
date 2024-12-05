    package com.example.musicplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityMain2Binding
import com.example.musicplayer.utils.MyData

    class MainActivity2 : AppCompatActivity() {
    lateinit var handler: Handler
    private val binding by lazy { ActivityMain2Binding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handler = Handler(Looper.getMainLooper())

        if (MyData.mediaPlayer!!.isPlaying){
            binding.play.setImageResource(R.drawable.img_9)
        }else{
            binding.play.setImageResource(R.drawable.img_1)
        }

        binding.play.setOnClickListener {
            if (MyData.mediaPlayer!!.isPlaying){
                MyData.mediaPlayer!!.pause()
                binding.play.setImageResource(R.drawable.img_1)
            }else{
                MyData.mediaPlayer!!.start()
                binding.play.setImageResource(R.drawable.img_9)
            }
        }
        binding.qaytish.setOnClickListener {
            finish()
        }

        binding.seekBar.max = MyData.mediaPlayer!!.duration
        handler.postDelayed(runnable,1000)
        binding.musicDuration.text = millisToTime(MyData.mediaPlayer!!.duration)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    MyData.mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // MyData.mediaPlayer.seekTo(binding.seekBar.progress)
            }
        })
        binding.musicTezOtish.setOnClickListener {
            MyData.mediaPlayer?.seekTo(MyData.mediaPlayer?.currentPosition?.plus(10000)?:0)
        }
        binding.musicTezQaytish.setOnClickListener {
            MyData.mediaPlayer?.seekTo(MyData.mediaPlayer?.currentPosition?.minus(10000)?:0)
        }
        binding.musicOtish.setOnClickListener {
            if (MyData.p!=MyData.list.size-1){
                MyData.mediaPlayer!!.stop()
                MyData.mediaPlayer = MediaPlayer.create(this, Uri.parse(MyData.list[MyData.p!!+1].musicPath))
                binding.musicDuration.text = millisToTime(MyData.mediaPlayer!!.duration)
                MyData.music = MyData.list[MyData.p!!.plus(1)]
                binding.seekBar.max =MyData.mediaPlayer!!.duration
                handler.postDelayed(runnable,1000)
                MyData.p =MyData.p!!+1
                MyData.mediaPlayer!!.start()
            }else{
                Toast.makeText(this, "Oxiriga keldingiz", Toast.LENGTH_SHORT).show()
            }
        }
        binding.musicQaytish.setOnClickListener {
            if (MyData.p!=0){
                MyData.mediaPlayer!!.stop()
                MyData.mediaPlayer = MediaPlayer.create(this,Uri.parse(MyData.list[MyData.p!!-1].musicPath))
                binding.musicDuration.text = millisToTime(MyData.mediaPlayer!!.duration)
                MyData.music = MyData.list[MyData.p!!.minus(1)]
                binding.seekBar.max =MyData.mediaPlayer!!.duration
                handler.postDelayed(runnable,1000)
                MyData.p =MyData.p!!-1
                MyData.mediaPlayer!!.start()
            }else{
                Toast.makeText(this, "Oldinga harakatlaning", Toast.LENGTH_SHORT).show()
            }
        }
        MyData.musicPosition.observe(this) {
            if (it == millisToTime(MyData.mediaPlayer!!.duration) && MyData.p != MyData.list.size - 1) {
                MyData.mediaPlayer =
                    MediaPlayer.create(this, Uri.parse(MyData.list[MyData.p!! + 1].musicPath))
                binding.musicDuration.text = millisToTime(MyData.mediaPlayer!!.duration)
                MyData.music = MyData.list[MyData.p!!.plus(1)]
                binding.seekBar.max = MyData.mediaPlayer!!.duration
                handler.postDelayed(runnable, 1000)
                MyData.p = MyData.p!! + 1
                MyData.mediaPlayer!!.start()
            }
        }
    }
        val runnable = object :Runnable {
            override fun run() {
                binding.musicPosition.text = millisToTime(MyData.mediaPlayer!!.currentPosition)
                MyData.musicPosition.postValue(binding.musicPosition.text.toString())
                println(MyData.musicPosition)
                binding.seekBar.progress = MyData.mediaPlayer!!.currentPosition
                handler.postDelayed(this, 1000)

            }
        }
        fun millisToTime(millis:Int):String{
            val minut = millis/60000
            val sekund = millis%60000/1000
            return "$minut:$sekund"
    }
}