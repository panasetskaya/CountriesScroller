package com.panasetskaia.countriesscroller.domain

import javax.inject.Inject

class LoadAllCountriesUseCase @Inject constructor(val repository: CountriesRepository) {

    suspend operator fun invoke(): NetworkResult<List<Country>> {
        return repository.loadAllCountries()
    }

}