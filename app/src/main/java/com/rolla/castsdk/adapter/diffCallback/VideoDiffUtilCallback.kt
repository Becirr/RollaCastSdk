package com.rolla.castsdk.adapter.diffCallback

import androidx.recyclerview.widget.DiffUtil
import com.rolla.castsdk.model.Video

class VideoDiffUtilCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(
        oldItem: Video,
        newItem: Video
    ): Boolean = oldItem.name == newItem.name

    override fun areContentsTheSame(
        oldItem: Video,
        newItem: Video
    ): Boolean = oldItem.name == newItem.name
}