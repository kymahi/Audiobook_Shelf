package com.kymahi.audiobookshelf.android

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    protected val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    protected val absRequest = AndroidABSRequest.INSTANCE

    protected fun showLoading() = mainActivity.setLoading(true)
    protected fun hideLoading() = mainActivity.setLoading(false)

    companion object {
        const val DEFAULT_SERVER_ID = -1
    }
}