package com.panasetskaia.countriesscroller.data.network

import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import retrofit2.http.GET

interface ApiService {

    @GET("all")
    suspend fun getAllCountries(
    ): ArrayList<CountryDto>

}