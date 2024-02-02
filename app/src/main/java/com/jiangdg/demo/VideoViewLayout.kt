package com.jiangdg.demo

import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.video_view_layout.idVideoView
import java.util.Objects

class VideoViewLayout : AppCompatActivity(){
    var videoUri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_view_layout)
        val intent = intent
        videoUri = Objects.requireNonNull<Bundle?>(intent.extras).getString("videoUri")
        idVideoView.setVideoURI(Uri.parse(videoUri))
        val mediaController = MediaController(this)
        mediaController.setAnchorView(idVideoView)
        // sets the media player to the videoView
        mediaController.setMediaPlayer(idVideoView)
        // sets the media controller to the videoView
        idVideoView.setMediaController(mediaController)
        idVideoView.start()
    }
}