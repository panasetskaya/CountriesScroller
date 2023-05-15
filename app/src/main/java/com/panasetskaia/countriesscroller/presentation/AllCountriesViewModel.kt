package com.panasetskaia.countriesscroller.presentation

import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.LoadAllCountriesUseCase
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.presentation.all_countries_screen.AllCountriesFragmentDirections
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesViewModel @Inject constructor(
    private val loadAllCountriesUseCase: LoadAllCountriesUseCase
) : BaseViewModel() {

    private val _countriesList =
        MutableStateFlow<NetworkResult<List<Country>>>(NetworkResult.loading())
    val countriesList: StateFlow<NetworkResult<List<Country>>> = _countriesList

    init {
        viewModelScope.launch {
            val countries = loadAllCountriesUseCase()
            _countriesList.value = countries
        }
    }

    fun goToCountryDetailsFragment(country: Country) {
        navigate(AllCountriesFragmentDirections.actionAllCountriesFragmentToDetailsFragment(country.commonName))
    }

}