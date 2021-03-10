package com.rolla.castsdk.utils

object Constants {

    // Namespace
    const val CAST_NAMESPACE = "urn:x-cast:com.google.cast.media"

    // Json params
    const val MEDIA_SESSION_ID = "mediaSessionId"
    const val REQUEST_ID = "requestId"
    const val TYPE = "type"

    // Commands
    const val COMMAND_PAUSE = "PAUSE"
    const val COMMAND_PLAY = "PLAY"
    const val COMMAND_STOP = "STOP"

    // Response
    const val STATUS = "status"
    const val PLAY_STATE = "playState"
    const val IDLE_REASON = "idleReason"

    // Player state
    const val IDLE = "IDLE"
    const val PAUSED = "PAUSED"
    const val BUFFERING = "BUFFERING"
    const val PLAYING = "PLAYING"
    const val FINISHED = "FINISHED"

    // Key
    const val KEY_EXTRA_SESSION_ID = "com.google.android.gms.cast.EXTRA_SESSION_ID"
}