package com.kymahi.audiobookshelf.android.setup.addserver.serverlist

import androidx.recyclerview.widget.RecyclerView
import com.app.sql.shared.entity.Server
import com.kymahi.audiobookshelf.android.databinding.ServerListItemBinding

class ServerViewHolder(private val binding: ServerListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(server: Server, onClickAction: (String, Int) -> Unit) {
        binding.server = server
        binding.root.setOnClickListener { onClickAction(server.url, server.id) }
    }
}