package com.kymahi.audiobookshelf.android.setup.addserver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.sql.shared.entity.Server
import com.kymahi.audiobookshelf.ABSRequest.Companion.getHost
import com.kymahi.audiobookshelf.android.*
import com.kymahi.audiobookshelf.android.setup.addserver.serverlist.ServerListAdapter
import com.kymahi.audiobookshelf.android.setup.addserver.viewmodels.AddServerModel
import com.kymahi.audiobookshelf.android.setup.viewmodels.GitHubViewModel
import com.kymahi.audiobookshelf.android.databinding.DialogFragmentAddServerBinding
import com.kymahi.audiobookshelf.android.databinding.FragmentAddServerBinding
import kotlinx.coroutines.launch
import kotlin.math.min

class AddServerFragment : BaseFragment() {
    private val gitHubModel by viewModels<GitHubViewModel>()
    private val addServerModel by viewModels<AddServerModel>()

    private lateinit var fragmentBinding: FragmentAddServerBinding
    private lateinit var dialogBinding: DialogFragmentAddServerBinding
    private lateinit var dialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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

        addServerModel.clearServersLiveData.observe(viewLifecycleOwner) {
            mainActivity.clearDatabase()
            addServerModel.updateServers(mainActivity.getAllServers())

            fragmentBinding.serverList.apply {
                val params = layoutParams
                params.height = 0
                layoutParams = params
            }
        }

        addServerModel.submitDialogLiveData.observe(viewLifecycleOwner) {
            showLoading()
            addServerModel.setError(false)
            lifecycleScope.launch {
                val url = dialogBinding.serverAddressInput.input
                if (mainActivity.getServerByUrl(url.getHost()) == null) {
                    absRequest.verifyServerAddress(url)
                } else {
                    addServerModel.setError(true, resources.getString(R.string.duplicate_server_error))
                    hideLoading()
                }
            }
        }

        addServerModel.cancelDialogLiveData.observe(viewLifecycleOwner) {
            hideLoading()
            dialog.reset()
        }

        addServerModel.onServerClickedLiveData.observe(viewLifecycleOwner) {
            navigateToLogin(it)
        }
    }

    private fun showLoading() = mainActivity.setLoading(true)
    private fun hideLoading() = mainActivity.setLoading(false)

    private fun setupServerDataFlow() {
        mainActivity.apply {
            setLoading(true)
            addServerModel.updateServers(getAllServers())
            setLoading(false)
        }

        lifecycleScope.launch {
            absRequest.validServerFlow.flowWithLifecycle(lifecycle).collect { url ->
                dialog.reset()
                mainActivity.apply {
                    insertServer(Server(DEFAULT_SERVER_ID, url))
                    addServerModel.updateServers(getAllServers())
                    fragmentBinding.serverList.apply {
                        val params = layoutParams
                        params.height = min(400.dp, height + resources.getDim(R.dimen.server_list_item_height))
                        layoutParams = params
                    }
                    setLoading(false)
                    navigateToLogin(url)
                }
            }
        }

        lifecycleScope.launch {
            absRequest.invalidServerFlow.flowWithLifecycle(lifecycle).collect {
                hideLoading()
                addServerModel.setError(true, resources.getString(R.string.server_connection_error))
            }
        }
    }

    private fun navigateToLogin(url: String) = findNavController().navigate(AddServerFragmentDirections.startLogin(url))

    private fun AlertDialog.reset() {
        dialogBinding.serverAddressInput.text.clear()
        addServerModel.setError(false)
        dismiss()
    }

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["serverList", "onItemClick"], requireAll = true)
        fun bindServerList(recyclerView: RecyclerView, serverList: List<Server>?, onItemClick: (String) -> Unit) {
            when (recyclerView.adapter) {
                is ServerListAdapter -> recyclerView.adapter as ServerListAdapter
                else -> ServerListAdapter(onClick = onItemClick).also { recyclerView.adapter = it }
            }.apply { updateServerList(serverList) }
        }
    }
}