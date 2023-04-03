package com.kymahi.audiobookshelf.android.addserver.serverlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.sql.shared.entity.Server
import com.kymahi.audiobookshelf.android.databinding.ServerListItemBinding

class ServerListAdapter(
    private val serverList: MutableList<Server> = mutableListOf()
) : RecyclerView.Adapter<ServerViewHolder>() {

    private lateinit var binding: ServerListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerViewHolder {
        binding = ServerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServerViewHolder, position: Int) = holder.bind(serverList[position])
    override fun getItemCount(): Int = serverList.size

    fun updateServerList(servers: List<Server>?) {
        val diffCallback = ServerDiffCallback(serverList, serverList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        serverList.clear()
        serverList.addAll(servers ?: emptyList())
        diffResult.dispatchUpdatesTo(this)
    }
}
