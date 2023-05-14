package com.panasetskaia.countriesscroller.presentation.allCountriesScreen

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.FragmentAllCountriesBinding
import com.panasetskaia.countriesscroller.domain.Status
import com.panasetskaia.countriesscroller.presentation.MainViewModel
import com.panasetskaia.countriesscroller.presentation.base.BaseFragment
import com.panasetskaia.countriesscroller.utils.getAppComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesFragment :
    BaseFragment<FragmentAllCountriesBinding, MainViewModel>(FragmentAllCountriesBinding::inflate) {

    @Inject
    override lateinit var viewModel: MainViewModel

    lateinit var listAdapter: CountriesListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }


    override fun onReady(savedInstanceState: Bundle?) {
        listAdapter = CountriesListAdapter {
            viewModel.goToCountryDetailsFragment(it)
        }
        binding.rViewCountries.adapter = listAdapter
        collectFlow()

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AllCountriesFragment()
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.countriesList.collectLatest {
                        when (it.status) {
                            Status.ERROR -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    R.string.loadingError,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            Status.LOADING -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                binding.progressBar.visibility = View.GONE
                                listAdapter.submitList(it.data)
                            }
                        }
                    }
                }
            }
        }
    }
}