package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.adapter.MusicAdapterim
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.models.Music
import com.example.musicplayer.utils.MyData

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var musicAdapterim: MusicAdapterim
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(MyData.music!=null){
            binding.name.text = MyData.music?.title
            binding.auothor.text = MyData.music?.author
        }
        binding.musicBar.setOnClickListener {
            if (MyData.music!=null) {
                val intent = Intent(this, MainActivity2::class.java)
                startActivity(intent)
            }
        }

        val list = musicFiles()
        MyData.list = list
        if (MyData.mediaPlayer!=null && MyData.mediaPlayer!!.isPlaying){
            binding.btnPlay.setImageResource(R.drawable.img_9)
        }else{
            binding.btnPlay.setImageResource(R.drawable.img_1)
        }
        binding.btnPlay.setOnClickListener {
            if (MyData.mediaPlayer!=null && MyData.mediaPlayer!!.isPlaying){
                MyData.mediaPlayer!!.pause()
                binding.btnPlay.setImageResource(R.drawable.img_1)
            }else if (MyData.mediaPlayer!=null && !MyData.mediaPlayer!!.isPlaying){
                MyData.mediaPlayer!!.start()
                binding.btnPlay.setImageResource(R.drawable.img_9)
            }
        }

        Log.d(TAG, "onCreate: $list")
        musicAdapterim = MusicAdapterim(list,object :MusicAdapterim.RvAction{
            override fun onClick(music: Music, position: Int) {
                val intent = Intent(this@MainActivity,MainActivity2::class.java)
                intent.putExtra("music",music)
                intent.putExtra("key",position)
                startActivity(intent)
                if (MyData.mediaPlayer!=null && !MyData.mediaPlayer!!.isPlaying) {
                    MyData.mediaPlayer = MediaPlayer.create(this@MainActivity, Uri.parse(list[position].musicPath))
                    MyData.mediaPlayer?.start()
                    MyData.music = music
                }else{
                    MyData.mediaPlayer?.stop()
                    MyData.mediaPlayer = MediaPlayer.create(this@MainActivity, Uri.parse(list[position].musicPath))
                    MyData.mediaPlayer?.start()
                    MyData.music = music
                }
                if (MyData.mediaPlayer!=null){
                    binding.name.text = MyData.music!!.title
                    binding.auothor.text = MyData.music!!.author
                }
                MyData.p = position
            }
        })
        binding.rv.adapter = musicAdapterim

    }
    @SuppressLint("Range")
    fun Context.musicFiles():ArrayList<Music>{
        val list:ArrayList<Music> = ArrayList()
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val cursor: Cursor? = this.contentResolver.query(
            uri,
            null,
            selection,
            null,
            sortOrder
        )

        if (cursor!= null && cursor.moveToFirst()){
            val id:Int = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val title:Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val imageId:Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)
            val authorId:Int = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)

            do {
                val audioId:Long = cursor.getLong(id)
                val audioTitle:String = cursor.getString(title)
                var imagePath = ""
                if (imageId !=-1){
                    imagePath = cursor.getString(imageId)
                }
                val musicPath:String = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val artist = cursor.getString(authorId)

                list.add(Music(audioId,audioTitle, imagePath, musicPath, artist))
            }while (cursor.moveToNext())
        }
        return  list

    }
}