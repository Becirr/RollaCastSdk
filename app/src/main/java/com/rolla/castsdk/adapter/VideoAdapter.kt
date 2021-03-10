package com.rolla.castsdk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rolla.castsdk.model.Video
import com.rolla.castsdk.adapter.diffCallback.VideoDiffUtilCallback
import com.rolla.castsdk.databinding.ItemVideoBinding

class VideoAdapter(
    private val listener: OnItemClickListener
) :
    ListAdapter<Video, VideoAdapter.VideoViewHolder>(VideoDiffUtilCallback()) {

    private var selectedItemPos = -1
    private var lastItemSelectedPos = -1
    var isClickable = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolder {
        val binding =
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvVideo.setOnClickListener {
                if (lastItemSelectedPos != adapterPosition && isClickable) {
                    selectedItemPos = adapterPosition
                    lastItemSelectedPos = if (lastItemSelectedPos == -1)
                        selectedItemPos
                    else {
                        getItem(lastItemSelectedPos).isSelected = false
                        notifyItemChanged(lastItemSelectedPos)
                        selectedItemPos
                    }
                    getItem(selectedItemPos).isSelected = true
                    notifyItemChanged(selectedItemPos)
                    listener.onItemClick(getItem(adapterPosition), adapterPosition)
                }
            }
        }

        fun bind(video: Video) {
            binding.tvVideo.text = video.name
            binding.tvVideo.isSelected = video.isSelected
        }

    }

    fun isAnyVideoSelected(): Boolean {
        return selectedItemPos != -1
    }

    interface OnItemClickListener {
        fun onItemClick(video: Video, position: Int)
    }

}