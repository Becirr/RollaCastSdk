package com.rolla.castsdk

import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.LaunchOptions
import com.google.android.gms.cast.LaunchOptions.Builder
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.rolla.castsdk.utils.Constants

class CastOptionsProvider : OptionsProvider {

    override fun getCastOptions(p0: Context?): CastOptions {
        /** Following lines enable Cast Connect */
        val launchOptions: LaunchOptions = Builder()
            .setAndroidReceiverCompatible(true)
            .build()
        return CastOptions.Builder()
            .setLaunchOptions(launchOptions)
            .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
            .setStopReceiverApplicationWhenEndingSession(true)
            .setSupportedNamespaces(mutableListOf(Constants.CAST_NAMESPACE))
            .build()
    }

    override fun getAdditionalSessionProviders(p0: Context?): MutableList<SessionProvider> {
        return mutableListOf()
    }
}