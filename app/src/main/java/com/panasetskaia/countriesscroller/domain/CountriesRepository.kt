package com.panasetskaia.countriesscroller.domain

interface CountriesRepository {

    suspend fun loadAllCountries(): NetworkResult<List<Country>>

}