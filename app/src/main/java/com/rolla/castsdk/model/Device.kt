package com.rolla.castsdk.model

import androidx.mediarouter.media.MediaRouter
import com.google.android.gms.cast.CastDevice

data class Device(
    val castDevice: CastDevice,
    val routeInfo: MediaRouter.RouteInfo,
    var isSelected: Boolean = false
)