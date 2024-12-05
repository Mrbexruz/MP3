package com.example.musicplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.ItemRvBinding
import com.example.musicplayer.models.Music

class MusicAdapterim(
    var list: ArrayList<Music>, // Bu musiqalar ro'yxati
    var rvAction: RvAction // Bu interfeys
) : RecyclerView.Adapter<MusicAdapterim.Vh>() {

    inner class Vh(private val itemRvBinding: ItemRvBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(music: Music, position: Int) {
            itemRvBinding.name.text = music.title
            itemRvBinding.author.text = music.author

            // Interfeys orqali element bosilganda aksiyani bajarish
            itemRvBinding.root.setOnClickListener {
                rvAction.onClick(music, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val binding = ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Vh(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    interface RvAction {
        fun onClick(music: Music, position: Int)
    }
}
