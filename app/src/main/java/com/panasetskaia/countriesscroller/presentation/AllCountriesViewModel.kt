package com.panasetskaia.countriesscroller.presentation

import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.LoadAllCountriesUseCase
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.presentation.all_countries_screen.AllCountriesFragmentDirections
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesViewModel @Inject constructor(
    private val loadAllCountriesUseCase: LoadAllCountriesUseCase
) : BaseViewModel() {

    private val _countriesList =
        MutableSharedFlow<NetworkResult<List<Country>>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val countriesList: SharedFlow<NetworkResult<List<Country>>> = _countriesList

    init {
        _countriesList.tryEmit(NetworkResult.loading())
        reloadCountries()
    }

    fun reloadCountries() {
        viewModelScope.launch {
            val countries = loadAllCountriesUseCase()
            _countriesList.tryEmit(countries)
        }
    }

    fun goToCountryDetailsFragment(country: Country) {
        navigate(AllCountriesFragmentDirections.actionAllCountriesFragmentToDetailsFragment(country.commonName))
    }

}