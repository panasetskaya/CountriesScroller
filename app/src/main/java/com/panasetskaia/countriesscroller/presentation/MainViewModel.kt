package com.panasetskaia.countriesscroller.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.data.repo.CountriesRepositoryImpl
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: CountriesRepositoryImpl) : ViewModel() {

    init {
        viewModelScope.launch {
            val countries = repo.loadAllCountries()
            Log.d("MY_TAG", countries.toString())
        }
    }

}