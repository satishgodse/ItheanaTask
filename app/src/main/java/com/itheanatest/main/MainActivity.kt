package com.itheanatest.main

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itheanatest.R
import com.itheanatest.api.ApiHelper
import com.itheanatest.api.RetrofitBuilder
import com.itheanatest.databinding.ActivityMainBinding
import com.itheanatest.main.adapter.SongsAdapter
import com.itheanatest.main.data.SongsData
import com.itheanatest.main.data.SongsModel
import com.itheanatest.main.data.SongsModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songsAdapter: SongsAdapter

    private val viewModel: SongsModel by viewModels {
        SongsModelFactory(
            ApiHelper(RetrofitBuilder.apiService)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupUI()
    }

    private fun setupUI() {

        /**
         * Show All Songs List using Recyclerview
         * **/
        val list = mutableListOf<SongsData>()
        val thumbnailPath = "https://images.macrumors.com/t/TZmMDWCFFrOQo7c9P4XCEmERXZI=/800x0/filters:quality(90)/article-new/2018/05/apple-music-note-800x420.jpg?lossy"
        val song = SongsData("Dhinchak", "/raw/sound.mp3",thumbnailPath,"Sonu Nigam, Anuradha Sriram")
        list.add(song)
        showData(list)
    }

    /**
     * Show All Songs using Recyclerview
     * **/
    private fun showData(list: MutableList<SongsData>) {
        val layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        binding.rvSongs.layoutManager = layoutManager
        songsAdapter = SongsAdapter()
        binding.rvSongs.adapter = songsAdapter
        songsAdapter.submitList(list)
    }
}