package com.panasetskaia.countriesscroller.presentation.details_screen

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.FragmentDetailsBinding
import com.panasetskaia.countriesscroller.domain.Country
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
                        setupBindingContent(it)
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

    private fun setupBindingContent(country: Country) {
        binding.topAppBarDetail.title = country.commonName
        val languages = country.languages
        if (languages.isNotEmpty() && detailsAreNotNull(country)) {
            val languagesAsString = languages.joinToString(", ")
            binding.tvCountryInfo.text = String.format(
                getString(R.string.country_info),
                country.commonName,
                country.subregion,
                country.population,
                languagesAsString,
                country.capital
            )
        }
        if (country.officialName!=null) {
            binding.officialName.text = country.officialName
        } else {
            binding.officialNameLabel.visibility = View.INVISIBLE
        }
        if (country.flagUrl != null) {
            Glide.with(requireContext()).load(country.flagUrl).centerCrop()
                .into(binding.imageViewBigFlag)
        }
    }

    private fun detailsAreNotNull(country: Country): Boolean {
        return country.subregion != null &&
                country.population != null &&
                country.capital != null
    }
}