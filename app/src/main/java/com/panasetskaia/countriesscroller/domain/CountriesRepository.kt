package com.panasetskaia.countriesscroller.domain

interface CountriesRepository {

    suspend fun loadAllCountries(): NetworkResult<List<Country>>

    suspend fun getCountryByName(commonName: String): Country

}