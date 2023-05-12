package com.panasetskaia.countriesscroller.data.repo

import com.panasetskaia.countriesscroller.data.mapper.CountryMapper
import com.panasetskaia.countriesscroller.data.network.ApiService
import com.panasetskaia.countriesscroller.domain.CountriesRepository
import com.panasetskaia.countriesscroller.domain.Country
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: CountryMapper
) : CountriesRepository {

    override suspend fun loadAllCountries(): List<Country> {
        val countryDtoList = apiService.getAllCountries()
        val result = mutableListOf<Country>()
        for (countryDto in countryDtoList) {
            val country = mapper.mapDtoToDomain(countryDto)
            result.add(country)
        }
        return result
    }
}