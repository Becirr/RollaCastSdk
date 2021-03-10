package com.rolla.castsdk

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.google.android.gms.cast.*
import com.google.android.gms.cast.framework.*
import com.rolla.castsdk.adapter.CastDeviceAdapter
import com.rolla.castsdk.adapter.VideoAdapter
import com.rolla.castsdk.databinding.ActivityMainBinding
import com.rolla.castsdk.model.Device
import com.rolla.castsdk.model.Video
import com.rolla.castsdk.utils.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mCastContext: CastContext
    private lateinit var mCastDeviceAdapter: CastDeviceAdapter
    private lateinit var mVideoAdapter: VideoAdapter
    private lateinit var mediaRouter: MediaRouter

    private var mCastSession: CastSession? = null
    private var selectedDevice: Device? = null
    private var selectedVideo: Video? = null
    private var playerState: String = Constants.IDLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupMediaRouter()
        setupListeners()
        mCastContext = CastContext.getSharedInstance(this)
    }

    private fun setupUI() {
        binding.rvDevices.setHasFixedSize(true)
        binding.rvVideos.setHasFixedSize(true)
        mCastDeviceAdapter = CastDeviceAdapter(castDeviceClickListener)
        binding.rvDevices.adapter = mCastDeviceAdapter
        mVideoAdapter = VideoAdapter(videoClickListener)
        binding.rvVideos.adapter = mVideoAdapter
        mVideoAdapter.submitList(ExoPlayerSamples.loadVideos())
    }

    private fun setupListeners() {
        binding.btnPlayPause.setOnClickListener {
            when (playerState) {
                Constants.PLAYING -> {
                    pause()
                }
                Constants.PAUSED -> {
                    resumePlaying()
                }
                Constants.IDLE -> {
                    play()
                }
            }
        }
        updatePlayButtonVisibility()
        binding.btnStop.setOnClickListener {
            stop()
        }
        updateStopButtonVisibility(false)
    }

    private fun loadVideo() {
        val mediaInfo = MediaInfo.Builder(selectedVideo!!.url)
            .setStreamType(selectedVideo!!.streamType)
            .setContentType(selectedVideo!!.mimeType)
            .build()
        mCastSession?.remoteMediaClient?.load(
            MediaLoadRequestData.Builder()
                .setMediaInfo(mediaInfo)
                .setAutoplay(true)
                .setCurrentTime(0).build()
        )
        mCastSession?.setMessageReceivedCallbacks(Constants.CAST_NAMESPACE, messageReceivedCallback)
    }

    private fun updateListDevices() {
        val devices = ArrayList<Device>()

        for (routeInfo in mediaRouter.routes) {
            val device = CastDevice.getFromBundle(routeInfo.extras)
            val extraSession = routeInfo.extras?.getString(Constants.KEY_EXTRA_SESSION_ID) != null
            if (device != null && !routeInfo.isDefault && !routeInfo.isBluetooth && routeInfo.isEnabled && !extraSession) {
                var isSelected = false
                for (oldRoute in mCastDeviceAdapter.currentList) {
                    if (oldRoute.castDevice.deviceId == device.deviceId) {
                        isSelected = oldRoute.isSelected
                    }
                }
                devices.add(Device(device, routeInfo, isSelected))
            }
        }
        mCastDeviceAdapter.submitList(devices)
        mCastDeviceAdapter.notifyDataSetChanged()
    }

    private fun updatePlayButtonVisibility() {
        val isEnabled =
            mCastDeviceAdapter.isAnyDeviceSelected() && mVideoAdapter.isAnyVideoSelected()
        binding.btnPlayPause.isClickable = isEnabled
        binding.btnPlayPause.alpha = if (isEnabled) 1f else 0.5f
    }

    private fun updateStopButtonVisibility(isEnabled: Boolean) {
        binding.btnStop.isClickable = isEnabled
        binding.btnStop.alpha = if (isEnabled) 1f else 0.5f
    }

    private fun play() {
        selectedDevice?.routeInfo?.select()
        updateProgressVisibility(true)
    }

    private fun pause() {
        mCastSession?.sendMessage(
            Constants.CAST_NAMESPACE,
            getMessage(mCastSession?.sessionId, Constants.COMMAND_PAUSE)
        )
        updateProgressVisibility(true)
    }

    private fun resumePlaying() {
        mCastSession?.sendMessage(
            Constants.CAST_NAMESPACE,
            getMessage(mCastSession?.sessionId, Constants.COMMAND_PLAY)
        )
        updateProgressVisibility(true)
    }

    private fun stop() {
        mCastSession?.sendMessage(
            Constants.CAST_NAMESPACE,
            getMessage(mCastSession?.sessionId, Constants.COMMAND_STOP)
        )
        playerState = Constants.FINISHED
        updateButtonsVisibility()
    }

    private fun setupMediaRouter() {
        mediaRouter = MediaRouter.getInstance(applicationContext)
        val mediaRouteSelector = MediaRouteSelector.Builder().addControlCategory(
            CastMediaControlIntent.categoryForCast(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
        ).build()
        mediaRouter.addCallback(
            mediaRouteSelector, mMediaRouterCallback,
            MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN
        )
    }

    private fun updateProgressVisibility(visible: Boolean) {
        if (visible) {
            binding.progress.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        } else {
            binding.progress.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun enableAdapters() {
        mCastDeviceAdapter.isClickable = true
        mVideoAdapter.isClickable = true
    }

    private fun disableAdapters() {
        mCastDeviceAdapter.isClickable = false
        mVideoAdapter.isClickable = false
    }

    private fun updateButtonsVisibility() {
        when (playerState) {
            Constants.BUFFERING -> {
            }
            Constants.PLAYING -> {
                updateProgressVisibility(false)
                updateStopButtonVisibility(true)
                disableAdapters()
                binding.btnPlayPause.text = resources.getString(R.string.pause)
            }
            Constants.PAUSED -> {
                updateProgressVisibility(false)
                updateStopButtonVisibility(true)
                disableAdapters()
                binding.btnPlayPause.text = resources.getString(R.string.resume)
            }
            Constants.IDLE -> {
                updateStopButtonVisibility(false)
                updatePlayButtonVisibility()
                enableAdapters()
                binding.btnPlayPause.text = resources.getString(R.string.play)
            }
            Constants.FINISHED -> {
                updateProgressVisibility(true)
                disableAdapters()
                CastContext.getSharedInstance(this).sessionManager.endCurrentSession(true)
            }
        }
    }

    override fun onResume() {
        mCastContext.addCastStateListener(mCastStateListener)
        mCastContext.sessionManager.addSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
        if (mCastSession == null) {
            mCastSession = CastContext.getSharedInstance(this).sessionManager.currentCastSession
        }
        super.onResume()
    }

    override fun onPause() {
        mCastContext.removeCastStateListener(mCastStateListener)
        mCastContext.sessionManager.removeSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
        mCastSession = null
        super.onPause()
    }

    public override fun onDestroy() {
        CastContext.getSharedInstance(this).sessionManager.currentCastSession?.sendMessage(
            Constants.CAST_NAMESPACE,
            getMessage(mCastSession?.sessionId, Constants.COMMAND_STOP)
        )
        CastContext.getSharedInstance(this).sessionManager.endCurrentSession(true)
        mediaRouter.removeCallback(mMediaRouterCallback)
        super.onDestroy()
    }

    private val castDeviceClickListener = object : CastDeviceAdapter.OnItemClickListener {
        override fun onItemClick(device: Device, position: Int) {
            selectedDevice = device
            updatePlayButtonVisibility()
        }
    }

    private val videoClickListener = object : VideoAdapter.OnItemClickListener {
        override fun onItemClick(video: Video, position: Int) {
            selectedVideo = video
            updatePlayButtonVisibility()
        }
    }

    private val mCastStateListener: CastStateListener = CastStateListener { newState ->
        if (newState != CastState.NO_DEVICES_AVAILABLE) {
            updateListDevices()
        }
    }

    private val mMediaRouterCallback = object : MediaRouter.Callback() {

        override fun onRouteSelected(router: MediaRouter?, info: MediaRouter.RouteInfo) {
            updateListDevices()
        }

        override fun onRouteUnselected(router: MediaRouter?, info: MediaRouter.RouteInfo?) {
            updateListDevices()
        }

        override fun onRouteAdded(router: MediaRouter?, route: MediaRouter.RouteInfo?) {
            updateListDevices()
        }

        override fun onRouteRemoved(router: MediaRouter?, route: MediaRouter.RouteInfo?) {
            updateListDevices()
        }

        override fun onRouteChanged(router: MediaRouter?, route: MediaRouter.RouteInfo?) {
            updateListDevices()
        }

        override fun onRouteUnselected(
            router: MediaRouter?,
            route: MediaRouter.RouteInfo?,
            reason: Int
        ) {
            updateListDevices()
        }
    }

    private val messageReceivedCallback =
        Cast.MessageReceivedCallback { _, _, message ->
            playerState = getPlayerState(message.toString())
            if (playerState == Constants.IDLE) {
                if (isFinished(message.toString())) {
                    playerState = Constants.FINISHED
                }
            }
            updateButtonsVisibility()
        }

    private val mSessionManagerListener: SessionManagerListener<CastSession> =
        object : SessionManagerListener<CastSession> {


            override fun onSessionStarting(p0: CastSession?) {

            }

            override fun onSessionStarted(session: CastSession?, p1: String?) {
                mCastSession = session
                if (mCastSession == null && selectedVideo == null) {
                    return
                }
                loadVideo()
            }

            override fun onSessionStartFailed(p0: CastSession?, p1: Int) {

            }

            override fun onSessionEnding(p0: CastSession?) {

            }

            override fun onSessionEnded(session: CastSession?, p1: Int) {
                updateProgressVisibility(false)
                playerState = Constants.IDLE
                updateButtonsVisibility()
                mCastSession = null
            }

            override fun onSessionResuming(p0: CastSession?, p1: String?) {

            }

            override fun onSessionResumed(session: CastSession?, p1: Boolean) {
                mCastSession = session
            }

            override fun onSessionResumeFailed(p0: CastSession?, p1: Int) {

            }

            override fun onSessionSuspended(p0: CastSession?, p1: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "Session suspended. Ending session",
                    Toast.LENGTH_LONG
                ).show()
                playerState = Constants.FINISHED
                updateButtonsVisibility()
            }

        }
}