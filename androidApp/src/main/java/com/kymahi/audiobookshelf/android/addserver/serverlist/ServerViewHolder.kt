package com.kymahi.audiobookshelf.android.addserver.serverlist

import androidx.recyclerview.widget.RecyclerView
import com.app.sql.shared.entity.Server
import com.kymahi.audiobookshelf.android.databinding.ServerListItemBinding

class ServerViewHolder(private val binding: ServerListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(server: Server) {
        binding.server = server
    }
}