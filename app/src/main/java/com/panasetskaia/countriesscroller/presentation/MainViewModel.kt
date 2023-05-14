package com.panasetskaia.countriesscroller.presentation

import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.LoadAllCountriesUseCase
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
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

    fun goToAllCountriesFragmentClicked() {
        //todo
    }

    fun goToCountryDetailsFragment(country: Country) {
        //todo
//        navigate(AllDogsFragmentDirections.actionAllDogsFragmentToDogDetailsFragment(breed))
    }

}