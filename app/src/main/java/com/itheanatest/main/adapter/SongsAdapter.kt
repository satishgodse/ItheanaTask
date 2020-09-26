package com.itheanatest.main.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itheanatest.R
import com.itheanatest.databinding.ItemSongBinding
import com.itheanatest.details.SongDetailsActivity
import com.itheanatest.main.data.SongsData

class SongsAdapter : ListAdapter<SongsData, SongsAdapter.ViewHolder>(SearchItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSongBinding>(inflater, R.layout.item_song, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataItem: SongsData) {

            /**
             * Show Songs when data is non null
             **/
                    binding.songItem = dataItem
                    binding.executePendingBindings()

            //  Click listener
            itemView.setOnClickListener {
                val intent = Intent(itemView.context,SongDetailsActivity::class.java);
                intent.putExtra("dataItem", dataItem)
                itemView.context.startActivity(intent);
            }
        }
    }

    class SearchItemDiffCallback : DiffUtil.ItemCallback<SongsData>() {
        override fun areItemsTheSame(old: SongsData, aNew: SongsData): Boolean {
            return old.songName == aNew.songName
        }

        override fun areContentsTheSame(old: SongsData, aNew: SongsData): Boolean {
            return old == aNew
        }
    }
}