package com.rolla.castsdk.adapter.diffCallback

import androidx.recyclerview.widget.DiffUtil
import com.rolla.castsdk.model.Device

class CastDeviceDiffUtilCallback : DiffUtil.ItemCallback<Device>() {
    override fun areItemsTheSame(
        oldItem: Device,
        newItem: Device
    ): Boolean = oldItem.castDevice.friendlyName == newItem.castDevice.friendlyName

    override fun areContentsTheSame(
        oldItem: Device,
        newItem: Device
    ): Boolean = oldItem.castDevice.friendlyName == newItem.castDevice.friendlyName
}