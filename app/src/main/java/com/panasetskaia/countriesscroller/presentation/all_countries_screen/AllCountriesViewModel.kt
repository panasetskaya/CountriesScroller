package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.LoadAllCountriesUseCase
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesViewModel @Inject constructor(
    private val loadAllCountriesUseCase: LoadAllCountriesUseCase
) : BaseViewModel() {

    private val _filterOptions = MutableStateFlow(FilteringOptions())
    val filterOptions: StateFlow<FilteringOptions> = _filterOptions

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

    fun changeFiltering(subregion: String?, position: Int = 0) {
        _filterOptions.value = FilteringOptions(subregion,position)
    }

    fun cancelFiltering() {
        _countriesList.tryEmit(NetworkResult.loading())
        reloadCountries()
        _filterOptions.value = FilteringOptions()
    }
}
