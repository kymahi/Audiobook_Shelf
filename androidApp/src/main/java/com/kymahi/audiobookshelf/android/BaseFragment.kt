package com.kymahi.audiobookshelf.android

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    val mainActivity: MainActivity?
        get() = activity as? MainActivity

    companion object {
        const val DEFAULT_SERVER_ID = -1L
    }
}