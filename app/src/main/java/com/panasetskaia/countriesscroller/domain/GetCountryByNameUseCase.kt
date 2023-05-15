package com.panasetskaia.countriesscroller.domain

import javax.inject.Inject

class GetCountryByNameUseCase @Inject constructor(private val repo: CountriesRepository) {

    suspend operator fun invoke(commonName: String): Country {
        return repo.getCountryByName(commonName)
    }

}