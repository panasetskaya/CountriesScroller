package com.panasetskaia.countriesscroller.data.repo

import com.panasetskaia.countriesscroller.domain.CountriesRepository
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.domain.Status

open class FakeRepository : CountriesRepository {

    var shouldReturnError = false
    var shouldReturnCountry = true

    override suspend fun loadAllCountries(): NetworkResult<List<Country>> {
        return if (!shouldReturnError)
            NetworkResult(Status.SUCCESS, listOf(), "")
        else
            NetworkResult(Status.ERROR, listOf(), "")
    }

    override suspend fun getCountryByName(commonName: String): Country? {
        return if (shouldReturnCountry)
            Country(
                commonName,
                null,
                null,
                listOf(),
                null,
                null,
                null
            )
        else null
    }
}