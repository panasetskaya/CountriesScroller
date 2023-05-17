package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.BottomSheetBinding
import com.panasetskaia.countriesscroller.databinding.FragmentAllCountriesBinding
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.Status
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
    private lateinit var searchView: SearchView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var listAdapter: CountriesListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onReady(savedInstanceState: Bundle?) {
        setupSwipeRefresh()
        bottomSheetDialog = BottomSheetDialog(requireContext())
        setAdapter()
        setMenuProvider()
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
                    viewModel.countriesList.collectLatest { result ->
                        when (result.status) {
                            Status.ERROR -> {
                                binding.swipeRefresh.isRefreshing = false
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    result.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (result.data != null) {
                                    listAdapter.submitList(result.data)
                                }
                            }
                            Status.LOADING -> {
                                binding.swipeRefresh.isRefreshing = false
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            Status.SUCCESS -> {
                                binding.swipeRefresh.isRefreshing = false
                                binding.progressBar.visibility = View.GONE
                                listAdapter.submitList(result.data)
                                setupSearch(result.data)
                            }
                        }
                    }
                }
                launch {
                    viewModel.filterOptions.collectLatest { filteringOptions ->
                        if (filteringOptions.subRegion != null) {
                            binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter).isVisible =
                                false
                            binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter_off).isVisible =
                                true
                        } else {
                            binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter).isVisible =
                                true
                            binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter_off).isVisible =
                                false
                        }
                        listAdapter.applyFilters(filteringOptions)
                    }
                }
            }
        }
    }

    private fun setMenuProvider() {
        binding.topAppBarMain.inflateMenu(R.menu.filter_menu)
        searchView = binding.topAppBarMain.menu.findItem(R.id.toolbar_search).actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE

        binding.topAppBarMain.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.toolbar_menu_filter -> {
                    showBottomSheetDialog()
                    true
                }
                R.id.toolbar_menu_filter_off -> {
                    viewModel.cancelFiltering()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun showBottomSheetDialog() {
        val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        setSpinner(bottomSheetBinding.spinnerSubregions, R.array.subregions_array)
        setSpinner(bottomSheetBinding.spinnerSorting, R.array.sort_options_array)
        bottomSheetBinding.applyFiltersButton.setOnClickListener {
            val selectedSubregion =
                if (bottomSheetBinding.spinnerSubregions.selectedItemPosition == 0) {
                    null
                } else {
                    bottomSheetBinding.spinnerSubregions.selectedItem as String?
                }
            val selectedSorting = bottomSheetBinding.spinnerSorting.selectedItemPosition
            viewModel.changeFiltering(selectedSubregion, selectedSorting)
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun setSpinner(spinner: Spinner, array: Int) {
        ArrayAdapter.createFromResource(
            this.requireContext(),
            array,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
            spinner.adapter = adapter
        }
    }

    private fun setupSearch(list: List<Country>?) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterByQuery(newText,list)
                return false
            }
        })
    }

    private fun filterByQuery(query: String?, list: List<Country>?
    ) {
        query?.let {
            val thereIs = list?.any { country ->
                country.commonName.lowercase().contains(query.lowercase()) }
            if (thereIs==true) {
                listAdapter.filterByQuery(it, list)
            } else {
                Toast.makeText(requireContext(), getString(R.string.not_found), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}