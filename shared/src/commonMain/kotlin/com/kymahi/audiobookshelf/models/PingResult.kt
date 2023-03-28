package com.kymahi.audiobookshelf.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PingResult(@SerialName("success") val isSuccess: Boolean)