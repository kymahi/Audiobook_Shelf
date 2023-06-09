package com.kymahi.audiobookshelf.android.setup.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kymahi.audiobookshelf.android.BaseFragment
import com.kymahi.audiobookshelf.android.R
import com.kymahi.audiobookshelf.android.setup.viewmodels.GitHubViewModel
import com.kymahi.audiobookshelf.android.databinding.FragmentLoginBinding
import com.kymahi.audiobookshelf.android.input
import com.kymahi.audiobookshelf.android.setup.login.viewmodels.LoginModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LoginFragment: BaseFragment() {
    private val gitHubModel by viewModels<GitHubViewModel>()
    private val loginModel by viewModels<LoginModel>()
    private val args: LoginFragmentArgs by navArgs()

    private lateinit var fragmentBinding: FragmentLoginBinding
    private lateinit var absUrl: String

    private var absId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        absUrl = args.absUrl
        absId = args.absId

        setupBinding()
        setupObservers()
        setupServerDataFlow()

        return fragmentBinding.root
    }

    private fun setupBinding() {
        fragmentBinding = FragmentLoginBinding.inflate(layoutInflater).apply {
            lifecycleOwner = viewLifecycleOwner
            loginModel = this@LoginFragment.loginModel
            githubModel = this@LoginFragment.gitHubModel
            url = absUrl
            backArrow.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupObservers() {
        gitHubModel.openLinkLiveData.observe(viewLifecycleOwner) { startActivity(it) }

        loginModel.loginLiveData.observe(viewLifecycleOwner) {
            showLoading()
            lifecycleScope.launch {
                loginModel.setError(false)
                absRequest.login(absUrl, fragmentBinding.usernameInput.input, fragmentBinding.passwordInput.input, absId)
            }
        }
    }

    private fun setupServerDataFlow() {
        lifecycleScope.launch {
            absRequest.validLoginFlow.flowWithLifecycle(lifecycle).collect {
                mainActivity.insertUser(it, absId.toLong())
                hideLoading()
            }
        }

        lifecycleScope.launch {
            absRequest.invalidLoginFlow.flowWithLifecycle(lifecycle).collect {
                loginModel.setError(true, resources.getString(R.string.login_failed_error))
                hideLoading()
            }
        }
    }
}