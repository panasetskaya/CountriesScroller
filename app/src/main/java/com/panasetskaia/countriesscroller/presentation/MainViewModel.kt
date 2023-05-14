package com.panasetskaia.countriesscroller.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.domain.CountriesRepository
import com.panasetskaia.countriesscroller.presentation.base.BaseViewModel
import com.panasetskaia.countriesscroller.utils.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: CountriesRepository
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            val countries = repo.loadAllCountries()
            Log.d(Constants.LOG_TAG, "countries are: $countries")
        }
    }

}