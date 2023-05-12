package com.panasetskaia.countriesscroller.data.repo

import android.util.Log
import com.panasetskaia.countriesscroller.data.mapper.CountryMapper
import com.panasetskaia.countriesscroller.data.network.ApiService
import com.panasetskaia.countriesscroller.domain.CountriesRepository
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.NetworkResult
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: CountryMapper
) : CountriesRepository {

    override suspend fun loadAllCountries(): NetworkResult<List<Country>> {
        return try {
            val countryDtoList = apiService.getAllCountries()
            val result = mutableListOf<Country>()
            for (countryDto in countryDtoList) {
                val country = mapper.mapDtoToDomain(countryDto)
                result.add(country)
            }
            NetworkResult.success(result)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
            NetworkResult.error()
        }
    }

    companion object {
        private const val LOG_TAG = "MYTAG"
    }
}