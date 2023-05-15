package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.FragmentAllCountriesBinding
import com.panasetskaia.countriesscroller.domain.Status
import com.panasetskaia.countriesscroller.presentation.AllCountriesViewModel
import com.panasetskaia.countriesscroller.presentation.base.BaseFragment
import com.panasetskaia.countriesscroller.utils.Constants
import com.panasetskaia.countriesscroller.utils.getAppComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesFragment :
    BaseFragment<FragmentAllCountriesBinding, AllCountriesViewModel>(FragmentAllCountriesBinding::inflate) {

    @Inject
    override lateinit var viewModel: AllCountriesViewModel

    lateinit var listAdapter: CountriesListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onReady(savedInstanceState: Bundle?) {
        setupSwipeRefresh()
        setAdapter()
        collectFlow()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.reloadCountries()
            Log.d(Constants.LOG_TAG, "swipeRefresh.setOnRefreshListener")
        }
    }

    private fun setAdapter() {
        listAdapter = CountriesListAdapter()
        listAdapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.rViewCountries.adapter = listAdapter
        listAdapter.onItemClickedListener = {
            viewModel.goToCountryDetailsFragment(it)
        }
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.countriesList.collectLatest {
                        when (it.status) {
                            Status.ERROR -> {
                                binding.swipeRefresh.isRefreshing = false
                                binding.progressBar.visibility = View.GONE
                                val errorMessage = String.format(getString(R.string.loadingError), it.msg)
                                Toast.makeText(
                                    requireContext(),
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (it.data!=null) {
                                    listAdapter.submitList(it.data)
                                }
                            }
                            Status.LOADING -> {
                                binding.swipeRefresh.isRefreshing = false
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                binding.swipeRefresh.isRefreshing = false
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