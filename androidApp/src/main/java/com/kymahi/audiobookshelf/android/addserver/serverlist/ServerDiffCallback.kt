package com.kymahi.audiobookshelf.android.addserver.serverlist

import androidx.recyclerview.widget.DiffUtil
import comjetbrainshandsonkmmsharedcache.Server

class ServerDiffCallback(private val oldList: List<Server>, private val newList: List<Server>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }
}