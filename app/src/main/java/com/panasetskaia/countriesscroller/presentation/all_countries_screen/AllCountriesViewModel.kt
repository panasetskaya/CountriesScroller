package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.LoadAllCountriesUseCase
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.domain.Status
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllCountriesViewModel @Inject constructor(
    private val loadAllCountriesUseCase: LoadAllCountriesUseCase
) : BaseViewModel() {

    private val screenStateInitial: ScreenState<List<Country>> = ScreenState.loading()
    private val innerCashList: MutableStateFlow<List<Country>> = MutableStateFlow(listOf())

    private val _screenState =
        MutableSharedFlow<ScreenState<List<Country>>>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val screenState: SharedFlow<ScreenState<List<Country>>> = _screenState

    init {
        _screenState.tryEmit(screenStateInitial)
        reloadCountries()
    }

    fun reloadCountries() {
        viewModelScope.launch {
            val networkResult = loadAllCountriesUseCase()
            when (networkResult.status) {
                Status.SUCCESS -> {
                    emitResult(networkResult, false)
                }
                Status.ERROR -> {
                    emitResult(networkResult, true)
                }
            }
        }
    }

    private fun emitResult(networkResult: NetworkResult<List<Country>>, withError: Boolean) {
        val errorState = if (withError) {
            ErrorState.error(networkResult.msg)
        } else ErrorState.perfect()
        val newScreenState = ScreenState.finished(
            errorState,
            networkResult.data,
            false
        )
        networkResult.data?.let {
            innerCashList.value = it
        }
        _screenState.tryEmit(newScreenState)
    }

    fun goToCountryDetailsFragment(country: Country) {
        navigate(AllCountriesFragmentDirections.actionAllCountriesFragmentToDetailsFragment(country.commonName))
    }

    fun changeFiltering(subregion: String?, position: Int = 0) {
        val data = applyFilters(FilteringOptions(subregion, position))
        val newScreenState = ScreenState.finished(
            ErrorState.perfect(),
            data,
            true
        )
        _screenState.tryEmit(newScreenState)
    }

    fun cancelFiltering() {
        val newScreenState = ScreenState.finished(
            ErrorState.perfect(),
            innerCashList.value,
            false
        )
        _screenState.tryEmit(newScreenState)
    }

    private fun applyFilters(options: FilteringOptions): List<Country> {
        var result = innerCashList.value
        if (options.subRegion != null)
            result = result.filter {
                it.subregion == options.subRegion
            }
        when (options.position) {
            SORT_BY_NAME -> {
                result = result.sortedBy {
                    it.commonName
                }
            }
            SORT_BY_POPULATION -> {
                result = result.sortedByDescending {
                    it.population
                }
            }
        }
        return result
    }

    fun filterByQuery(query: String) {
        val initialList = innerCashList.value
        val result = initialList.filter { country ->
            country.commonName.lowercase().contains(query.lowercase())
        }
        val newScreenState = ScreenState.finished(
            ErrorState.perfect(),
            result,
            false
        )
        _screenState.tryEmit(newScreenState)
    }

    companion object {
        private const val SORT_BY_NAME = 0
        private const val SORT_BY_POPULATION = 1

    }
}
