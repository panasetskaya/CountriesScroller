package com.panasetskaia.countriesscroller.data.network

import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import retrofit2.http.GET
import java.net.UnknownHostException

interface ApiService {

    @Throws(Exception::class)
    @GET("all")
    suspend fun getAllCountries(
    ): ArrayList<CountryDto>

}