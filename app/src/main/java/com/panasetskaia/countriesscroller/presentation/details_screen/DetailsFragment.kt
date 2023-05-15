package com.panasetskaia.countriesscroller.presentation.details_screen

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.panasetskaia.countriesscroller.databinding.FragmentDetailsBinding
import com.panasetskaia.countriesscroller.presentation.base.BaseFragment
import com.panasetskaia.countriesscroller.utils.getAppComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsFragment :
    BaseFragment<FragmentDetailsBinding, DetailsViewModel>(FragmentDetailsBinding::inflate) {

    @Inject
    override lateinit var viewModel: DetailsViewModel

    private val navArgs by navArgs<DetailsFragmentArgs>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.getCountryByName(navArgs.commonName)
        setAppBar()
        collectFlow()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.country.collectLatest {

                    }
                }
            }
        }
    }

    private fun setAppBar() {
        binding.topAppBarDetail.setNavigationOnClickListener {
            viewModel.navigateBack()
            true
        }
    }
}