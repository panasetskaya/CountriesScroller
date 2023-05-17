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

    private val initialValue: NetworkResult<List<Country>> = NetworkResult.loading()

    private val _innerCashCountries = MutableStateFlow(initialValue)

    private val _filterOptions = MutableStateFlow(FilteringOptions())
    val filterOptions: StateFlow<FilteringOptions> = _filterOptions

    private val _countriesList =
        MutableSharedFlow<NetworkResult<List<Country>>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val countriesList: SharedFlow<NetworkResult<List<Country>>> = _countriesList

    init {
        _countriesList.tryEmit(initialValue)
        reloadCountries()
    }

    fun reloadCountries() {
        viewModelScope.launch {
            _innerCashCountries.value = loadAllCountriesUseCase()
            _countriesList.tryEmit(_innerCashCountries.value)
        }
    }

    fun goToCountryDetailsFragment(country: Country) {
        navigate(AllCountriesFragmentDirections.actionAllCountriesFragmentToDetailsFragment(country.commonName))
    }

    fun changeFiltering(subregion: String?, position: Int = 0) {
        _filterOptions.value = FilteringOptions(subregion,position)
    }

    fun cancelFiltering() {
        viewModelScope.launch {
            _filterOptions.value = FilteringOptions()
            _countriesList.tryEmit(_innerCashCountries.value)
        }

    }
}
