package com.rolla.castsdk.utils

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

fun getMessage(sessionId: String?, command: String): String {
    val json = JSONObject()
    json.put(Constants.MEDIA_SESSION_ID, sessionId)
    json.put(Constants.REQUEST_ID, "0")
    json.put(Constants.TYPE, command)
    return json.toString()
}

fun getPlayerState(json: String): String {
    val jsonObject = JSONObject(json)
    val jsonArray = jsonObject.get(Constants.STATUS) as JSONArray
    val statusObject = jsonArray.get(0) as JSONObject
    val playState = statusObject.get(Constants.PLAY_STATE).toString()
    Log.e("play state", playState)
    return playState
}

fun isFinished(json: String): Boolean {
    val jsonObject = JSONObject(json)
    val jsonArray = jsonObject.get(Constants.STATUS) as JSONArray
    val statusObject = jsonArray.get(0) as JSONObject
    val hasIdleReason = statusObject.has(Constants.IDLE_REASON)
    Log.e("has idle reason", hasIdleReason.toString())
    return if (hasIdleReason) {
        val idleReason = statusObject.getString(Constants.IDLE_REASON)
        idleReason == Constants.FINISHED
    } else {
        false
    }
}

