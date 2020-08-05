package com.example.app_movie.Video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*
import android.content.pm.ActivityInfo
import com.example.app_movie.R


class VideoActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private lateinit var adsLoader: ImaAdsLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        adsLoader = ImaAdsLoader(this, VideoModel.AD_TAG_URI)
    }

    override fun onStart() {
        super.onStart()
        player = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        playerView.player = player

        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )

        val mediaSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(VideoModel.MP4_URI)

        val adsMediaSource = AdsMediaSource(
            mediaSource, dataSourceFactory, adsLoader,
            playerView.overlayFrameLayout
        )

        player?.prepare(adsMediaSource)
        player?.playWhenReady = true

    }

    override fun onRestart() {
        super.onRestart()
        player?.release()
    }

    override fun onStop() {
        super.onStop()
        player?.release()
    }

    override fun onDestroy() {
        super.onDestroy()

        adsLoader.release()
    }
}
