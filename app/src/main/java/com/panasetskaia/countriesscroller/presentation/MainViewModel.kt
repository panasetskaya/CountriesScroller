package com.panasetskaia.countriesscroller.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.countriesscroller.data.repo.CountriesRepositoryImpl
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val repo = CountriesRepositoryImpl()

    init {
        viewModelScope.launch {
            val countries = repo.loadAllCountries()
            Log.d("MY_TAG", countries.toString())
        }
    }

}