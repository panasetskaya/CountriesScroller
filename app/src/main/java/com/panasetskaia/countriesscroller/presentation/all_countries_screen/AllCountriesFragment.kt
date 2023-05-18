package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.BottomSheetBinding
import com.panasetskaia.countriesscroller.databinding.FragmentAllCountriesBinding
import com.panasetskaia.countriesscroller.di.viewmodel.ViewModelFactory
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.Status
import com.panasetskaia.countriesscroller.presentation.base.BaseFragment
import com.panasetskaia.countriesscroller.utils.getAppComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesFragment :
    BaseFragment<FragmentAllCountriesBinding, AllCountriesViewModel>(FragmentAllCountriesBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val viewModel by viewModels<AllCountriesViewModel> { viewModelFactory }
    private lateinit var searchView: SearchView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var listAdapter: CountriesListAdapter
    private lateinit var spinnerSubregionsAdapter: ArrayAdapter<CharSequence?>
    private lateinit var spinnerSortByAdapter: ArrayAdapter<CharSequence?>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onReady(savedInstanceState: Bundle?) {
        setupSwipeRefresh()
        bottomSheetDialog = BottomSheetDialog(requireContext())
        setAdapters()
        setMenuProvider()
        collectFlow()
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.reloadCountries()
        }
    }

    private fun setAdapters() {
        listAdapter = CountriesListAdapter()
        with(listAdapter) {
            stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            binding.rViewCountries.adapter = this
            onItemClickedListener = {
                viewModel.goToCountryDetailsFragment(it)
            }
        }
        setSortByAdapter()
        setNewListForSubregionsSpinner(
            resources.getStringArray(R.array.subregions_array).toList()
        )
    }

    private fun setSortByAdapter() {
        spinnerSortByAdapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.sort_options_array,
            R.layout.item_spinner
        ).also {
            it.setDropDownViewResource(R.layout.item_spinner_dropdown)
        }
    }

    private fun setNewListForSubregionsSpinner(
        list: List<String?>
    ) {
        val listToSubmit = list.filterNotNull()
        spinnerSubregionsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            listToSubmit
        )
        spinnerSubregionsAdapter.also {
            it.setDropDownViewResource(R.layout.item_spinner_dropdown)
        }

    }

    private fun collectFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.countriesList.collectLatest { result ->
                        when (result.status) {
                            Status.ERROR -> {
                                hideProgressBar()
                                Toast.makeText(
                                    requireContext(),
                                    result.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                                result.data?.let {
                                    listAdapter.submitList(it)
                                }

                            }
                            Status.LOADING -> {
                                showProgressBar()
                            }
                            Status.SUCCESS -> {
                                hideProgressBar()
                                listAdapter.submitList(result.data)
                                setSubregions(result.data)
                                setupSearch(result.data)
                            }
                        }
                    }
                }
                launch {
                    viewModel.filterOptions.collectLatest { filteringOptions ->
                        if (filteringOptions.subRegion != null) {
                            showFilterOffIcon()
                        } else {
                            showFilterIcon()
                        }
                        listAdapter.applyFilters(filteringOptions)
                    }
                }
            }
        }
    }

    private fun setSubregions(data: List<Country>?) {
        val subregions: MutableList<String?> = mutableListOf(getString(R.string.default_category))
        val dataSubregions = data?.map {
            it.subregion
        }
        dataSubregions?.let {
            subregions.addAll(it.toSet())
            setNewListForSubregionsSpinner(subregions.toList())
        }

    }

    private fun showProgressBar() {
        binding.swipeRefresh.isRefreshing = false
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.swipeRefresh.isRefreshing = false
        binding.progressBar.visibility = View.GONE
    }

    private fun showFilterIcon() {
        binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter).isVisible =
            true
        binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter_off).isVisible =
            false
    }

    private fun showFilterOffIcon() {
        binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter).isVisible =
            false
        binding.topAppBarMain.menu.findItem(R.id.toolbar_menu_filter_off).isVisible =
            true
    }

    private fun setMenuProvider() {
        binding.topAppBarMain.inflateMenu(R.menu.filter_menu)
        searchView =
            binding.topAppBarMain.menu.findItem(R.id.toolbar_search).actionView as SearchView
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
        with(bottomSheetBinding) {
            bottomSheetDialog.setContentView(root)
            spinnerSubregions.adapter = spinnerSubregionsAdapter
            spinnerSorting.adapter = spinnerSortByAdapter
            applyFiltersButton.setOnClickListener {
                val selectedSubregion =
                    if (spinnerSubregions.selectedItemPosition == 0) {
                        null
                    } else {
                        spinnerSubregions.selectedItem as String?
                    }
                val selectedSorting = spinnerSorting.selectedItemPosition
                viewModel.changeFiltering(selectedSubregion, selectedSorting)
                bottomSheetDialog.dismiss()
            }
        }
        bottomSheetDialog.show()
    }

    private fun setupSearch(list: List<Country>?) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterByQuery(newText, list)
                return false
            }
        })
    }

    private fun filterByQuery(
        query: String?, list: List<Country>?
    ) {
        query?.let {
            val thereIs = list?.any { country ->
                country.commonName.lowercase().contains(query.lowercase())
            }
            if (thereIs == true) {
                listAdapter.filterByQuery(it, list)
            } else {
                Toast.makeText(requireContext(), getString(R.string.not_found), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}