package com.rolla.castsdk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rolla.castsdk.adapter.diffCallback.CastDeviceDiffUtilCallback
import com.rolla.castsdk.model.Device
import com.rolla.castsdk.databinding.ItemCastDeviceBinding

class CastDeviceAdapter(
    private val listener: OnItemClickListener
) :
    ListAdapter<Device, CastDeviceAdapter.CastDeviceViewHolder>(CastDeviceDiffUtilCallback()) {

    private var selectedItemPos = -1
    private var lastItemSelectedPos = -1
    var isClickable = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CastDeviceViewHolder {
        val binding =
            ItemCastDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastDeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastDeviceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CastDeviceViewHolder(private val binding: ItemCastDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deviceName.setOnClickListener {
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

        fun bind(device: Device) {
            binding.deviceName.text = device.castDevice.friendlyName
            binding.deviceName.isSelected = device.isSelected
        }

    }

    override fun submitList(list: MutableList<Device>?) {
        super.submitList(list)
        if (itemCount == 0) {
            selectedItemPos = -1
            lastItemSelectedPos = -1
        }
    }

    fun isAnyDeviceSelected(): Boolean {
        return selectedItemPos != -1
    }

    interface OnItemClickListener {
        fun onItemClick(device: Device, position: Int)
    }

}