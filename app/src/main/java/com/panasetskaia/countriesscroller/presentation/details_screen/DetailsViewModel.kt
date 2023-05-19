package com.panasetskaia.countriesscroller.presentation.details_screen

import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.GetCountryByNameUseCase
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val getCountryByNameUseCase: GetCountryByNameUseCase
) : BaseViewModel() {

    private val _country =
        MutableSharedFlow<Country>(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val country: SharedFlow<Country> = _country

    fun getCountryByName(commonName: String) {
        viewModelScope.launch {
            val countryByName = getCountryByNameUseCase(commonName)
            countryByName?.let {
                _country.tryEmit(it)
            }
        }
    }

    fun detailsAreNotNull(country: Country): Boolean {
        return country.subregion != null && country.subregion != "" &&
                country.population != null &&
                country.capital != null && country.capital != ""
    }

    fun convertToThousands(population: Int): Int {
        return population / 1000
    }
}