package com.panasetskaia.countriesscroller.data.repo

import com.panasetskaia.countriesscroller.data.network.ApiFactory
import com.panasetskaia.countriesscroller.data.mapper.CountryMapper
import com.panasetskaia.countriesscroller.domain.CountriesRepository
import com.panasetskaia.countriesscroller.domain.Country

class CountriesRepositoryImpl : CountriesRepository {

    private val apiFactory = ApiFactory
    private val service = apiFactory.apiService
    private val mapper = CountryMapper()

    override suspend fun loadAllCountries(): List<Country> {
        val countryDtoList = service.getAllCountries()
        val result = mutableListOf<Country>()
        for (countryDto in countryDtoList) {
            val country = mapper.mapDtoToDomain(countryDto)
            result.add(country)
        }
        return result
    }
}