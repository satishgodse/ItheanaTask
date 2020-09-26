package com.itheanatest.details

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.itheanatest.R
import com.itheanatest.api.ApiHelper
import com.itheanatest.api.RetrofitBuilder
import com.itheanatest.databinding.ActivitySongDetailsBinding
import com.itheanatest.details.data.SongDetailsViewModel
import com.itheanatest.details.data.SongsDetailsViewModelFactory
import com.itheanatest.main.data.SongsData
import com.itheanatest.services.BackgroundSoundService
import com.itheanatest.utils.Constants


class SongDetailsActivity : AppCompatActivity() {

    private var dataItem: SongsData? = null
    private lateinit var binding: ActivitySongDetailsBinding
    lateinit var mBoundService: BackgroundSoundService
    var mServiceBound = false
    var CurrentPosition:Int = 0
    var totalDuration:Int = 0
    var myBinder : BackgroundSoundService.LocalBinder? = null

    private val viewModel: SongDetailsViewModel by viewModels {
        SongsDetailsViewModelFactory(
            ApiHelper(RetrofitBuilder.apiService)
        )
    }


    lateinit var mainHandler: Handler
    private val updateTextTask = object : Runnable {
        override fun run() {
            getSeekBarStatus(myBinder!!)
            mainHandler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_song_details)

        // Get Data Item Passed from Main List Screen
        dataItem = intent.getParcelableExtra("dataItem")
        binding.songDetailsItem = dataItem

        binding.ivPlay.setOnClickListener{
            val service = Intent(this, BackgroundSoundService::class.java)
            service.action = Constants.ACTION.STARTFOREGROUND_ACTION
            startService(service)
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            getSeekBarStatus(myBinder!!)
            mainHandler.post(updateTextTask)

        }
        mainHandler = Handler(Looper.getMainLooper())
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            myBinder = service as BackgroundSoundService.LocalBinder
            mBoundService = myBinder!!.getService()
            mServiceBound = true
            getSeekBarStatus(myBinder!!)
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, BackgroundSoundService::class.java).also { intent ->
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
            //getSeekBarStatus(myBinder);
        }
    }

    //This method is used to update seekBar status by getting Player Current Position from service.
    fun getSeekBarStatus(myBinder: BackgroundSoundService.LocalBinder) {
        Thread(Runnable {
            var CurrentPosition = 0
            var totalDuration = 0
            var mCurrentPosition = 0
            totalDuration = getMediaPlayerTotalDuration(myBinder)
            binding.seekBar.max = totalDuration

//            while(CurrentPosition<total) {
                try {
                    Thread.sleep(1000)
                    CurrentPosition = getMediaPlayerCurrentPosition(myBinder)
                    mCurrentPosition = CurrentPosition / 1000
                    Log.d("TAG", mCurrentPosition.toString())
                } catch (e: InterruptedException) {
                    return@Runnable
                }
//            }
                binding.seekBar.setProgress(mCurrentPosition)

        }).start()
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                ProgressValue: Int,
                fromUser: Boolean
            ) {
//                 if(fromUser){
//                   mBoundService.player!!.seekTo(ProgressValue);
//                }
                val Minutes = (ProgressValue / 1000 / 60).toLong()
                val Seconds = ProgressValue / 1000 % 60
                binding.tvLength.setText("$Minutes:$Seconds")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    //This method get MediaPlayerCurrent Position from service
    fun getMediaPlayerCurrentPosition(myBinder: BackgroundSoundService.LocalBinder): Int {
        if (mServiceBound) {
            //if (mBoundService.player != null) {
                CurrentPosition = myBinder.seekBarGetCurrentPosition()
           // }
        }
        return CurrentPosition
    }

    //This method get MediaPlayerCurrent Position from service
    fun getMediaPlayerTotalDuration(myBinder: BackgroundSoundService.LocalBinder): Int {
        if (mServiceBound) {
            totalDuration = myBinder.getTotalSongDuration()
        }
        return CurrentPosition
    }

    override fun onStop() {
        super.onStop()
        mainHandler = Handler(Looper.getMainLooper())
    }
}