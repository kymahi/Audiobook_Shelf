package com.kymahi.audiobookshelf.android.addserver

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.util.TypedValue.applyDimension
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.kymahi.audiobookshelf.ABSRequest
import com.kymahi.audiobookshelf.android.BaseFragment
import com.kymahi.audiobookshelf.android.R
import com.kymahi.audiobookshelf.android.addserver.serverlist.ServerListAdapter
import com.kymahi.audiobookshelf.android.addserver.viewmodels.AddServerModel
import com.kymahi.audiobookshelf.android.addserver.viewmodels.GitHubViewModel
import com.kymahi.audiobookshelf.android.databinding.DialogFragmentAddServerBinding
import com.kymahi.audiobookshelf.android.databinding.FragmentAddServerBinding
import comjetbrainshandsonkmmsharedcache.Server
import kotlinx.coroutines.launch
import kotlin.math.min

class AddServerFragment : BaseFragment() {
    private val gitHubModel by viewModels<GitHubViewModel>()
    private val addServerModel by viewModels<AddServerModel>()
    private val absRequest = ABSRequest()

    private lateinit var fragmentBinding: FragmentAddServerBinding
    private lateinit var dialogBinding: DialogFragmentAddServerBinding
    private lateinit var dialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setupBindings()

        dialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root).create()

        observeLiveData()
        setupServerDataFlow()

        return fragmentBinding.root
    }

    private fun setupBindings() {
        fragmentBinding = FragmentAddServerBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
            githubModel = this@AddServerFragment.gitHubModel
            addServerModel = this@AddServerFragment.addServerModel
            serverList.setHasFixedSize(false)
        }

        dialogBinding = DialogFragmentAddServerBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
            addServerModel = this@AddServerFragment.addServerModel
        }
    }

    private fun observeLiveData() {
        gitHubModel.openLinkLiveData.observe(viewLifecycleOwner) { startActivity(it) }

        addServerModel.addServerModalLiveData.observe(viewLifecycleOwner) { dialog.show() }

        addServerModel.submitDialogLiveData.observe(viewLifecycleOwner) {
            addServerModel.showError(false)
            lifecycleScope.launch {
                absRequest.verifyServerAddress(dialogBinding.serverAddressInput.text.toString())
            }
        }

        addServerModel.cancelDialogLiveData.observe(viewLifecycleOwner) {
            dialog.reset()
        }
    }

    private fun setupServerDataFlow() {
        lifecycleScope.launch {
            addServerModel.updateServers(mainActivity?.getAllServers())

            absRequest.validServerFlow.flowWithLifecycle(lifecycle).collect { url ->
                dialog.reset()
                mainActivity?.insertServer(Server(DEFAULT_SERVER_ID, url))?.let {
                    addServerModel.updateServers(mainActivity?.getAllServers())
                    fragmentBinding.serverList.apply {
                        val params = layoutParams
                        params.height = min(400.dp, height + 56.dp).toInt()
                        layoutParams = params
                    }
                }
            }

            absRequest.invalidServerFlow.flowWithLifecycle(lifecycle).collect {
                addServerModel.showError(true)
            }
        }
    }

    private fun AlertDialog.reset() {
        dialogBinding.serverAddressInput.text.clear()
        addServerModel.showError(false)
        dismiss()
    }

    private val Number.dp get() = applyDimension(COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

    companion object {
        @JvmStatic
        @BindingAdapter("serverList")
        fun bindServerList(recyclerView: RecyclerView, servers: List<Server>?) {
            when (recyclerView.adapter) {
                is ServerListAdapter -> recyclerView.adapter as ServerListAdapter
                else -> ServerListAdapter().also { recyclerView.adapter = it }
            }.apply { updateServerList(servers) }
        }
    }
}