package com.itheanatest.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.itheanatest.R
import com.itheanatest.main.MainActivity
import com.itheanatest.utils.Constants
import java.util.*


class BackgroundSoundService : Service() {
    var player: MediaPlayer? = null
    val CHANNEL_ID = "ForegroundServiceChannel"
    var total = 0
    var CurrentPosition:Int = 0
    private val mBinder: IBinder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

     override fun onRebind(intent: Intent?) {
         Log.v("LOG_TAG", "in onRebind")
         super.onRebind(intent)
     }

     override fun onUnbind(intent: Intent?): Boolean {
         Log.v("LOG_TAG", "in onUnbind")
         return true
     }

    override fun onCreate() {
        super.onCreate()

        player = MediaPlayer()
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player = MediaPlayer.create(this, R.raw.sound);
        player!!.setLooping(true); // Set looping
        player!!.setVolume(100F, 100F);

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if(!player!!.isPlaying()){
            player!!.start();
        }else{
            player!!.pause();
        }




        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification(intent);

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play!", Toast.LENGTH_SHORT).show();
        }else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }


    override fun onStart(intent: Intent, startId: Int) {
        // TO DO
    }

    fun onUnBind(arg0: Intent): IBinder? {
        // TO DO Auto-generated method
        return null
    }

    fun onStop() {

    }

    fun onPause() {

    }

    override fun onDestroy() {
        super.onDestroy();
        Log.v("LOG_TAG", "in onDestroy");
        player!!.stop()
        player!!.release()
    }

    override fun onLowMemory() {

    }

    companion object {
        private val TAG: String? = null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(intent: Intent) {
        var status: Notification
        val views = RemoteViews(
            packageName,
            R.layout.status_bar
        )
        val bigViews = RemoteViews(
            packageName,
            R.layout.status_bar_expanded
        )

        val previousIntent = Intent(this, BackgroundSoundService::class.java)
        previousIntent.action = Constants.ACTION.PREV_ACTION
        val ppreviousIntent = PendingIntent.getService(
            this, 0,
            previousIntent, 0
        )

        val playIntent = Intent(this, BackgroundSoundService::class.java)
        playIntent.action = Constants.ACTION.PLAY_ACTION
        val pplayIntent = PendingIntent.getService(
            this, 0,
            playIntent, 0
        )

        val nextIntent = Intent(this, BackgroundSoundService::class.java)
        nextIntent.action = Constants.ACTION.NEXT_ACTION
        val pnextIntent = PendingIntent.getService(
            this, 0,
            nextIntent, 0
        )

        val closeIntent = Intent(this, BackgroundSoundService::class.java)
        closeIntent.action = Constants.ACTION.STOPFOREGROUND_ACTION
        val pcloseIntent = PendingIntent.getService(
            this, 0,
            closeIntent, 0
        )

        views.setOnClickPendingIntent(R.id.iv_play, pplayIntent)
        bigViews.setOnClickPendingIntent(R.id.iv_play, pplayIntent)

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent)
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent)

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent)
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent)


        views.setImageViewResource(
            R.id.status_bar_play,
            R.drawable.btn_play
        )
        bigViews.setImageViewResource(
            R.id.status_bar_play,
            R.drawable.btn_play
        )

        views.setTextViewText(R.id.status_bar_track_name, "Demo Song")
        bigViews.setTextViewText(R.id.status_bar_track_name, "Demo Song")

        views.setTextViewText(R.id.status_bar_artist_name, "Kumar Sanu, Anuradha Sriram")
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Kumar Sanu, Anuradha Sriram")

        bigViews.setTextViewText(R.id.status_bar_album_name, "Music")


        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Demo Song")
            .setContentText(input)
            .setSmallIcon(R.drawable.logo)
            .setContentIntent(pendingIntent)
            .build()

        notification.contentView = views
        notification.bigContentView = bigViews
        startForeground(1, notification)

    }

    inner class LocalBinder : Binder() {
        // Return this instance of BackgroundSoundService so clients can call public methods
        fun getService(): BackgroundSoundService = BackgroundSoundService()

        fun seekBarGetCurrentPosition(): Int { //This method is created to get SongCurrentPosition from mediaplayer for seekbar

            if (player != null && player!!.isPlaying()) {
                CurrentPosition = player!!.getCurrentPosition()
            }
            return CurrentPosition
        }

        fun getTotalSongDuration(): Int { //This method is created to get SongCurrentPosition from mediaplayer for seekbar

            var duration = 0
            if (player != null) {
                duration = player!!.duration
            }
            return duration!!.toInt()
        }
    }

    fun onCompletion(mediaPlayer: MediaPlayer?) {
        stopSelf()
    }
}
