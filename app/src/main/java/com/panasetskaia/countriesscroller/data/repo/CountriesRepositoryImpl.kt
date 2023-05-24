package com.panasetskaia.countriesscroller.data.repo

import android.util.Log
import com.panasetskaia.countriesscroller.data.local.CountryDao
import com.panasetskaia.countriesscroller.data.mapper.CountryMapper
import com.panasetskaia.countriesscroller.data.network.ApiService
import com.panasetskaia.countriesscroller.domain.CountriesRepository
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.NetworkResult
import com.panasetskaia.countriesscroller.utils.Constants.LOG_TAG
import java.net.UnknownHostException
import javax.inject.Inject

class CountriesRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mapper: CountryMapper,
    private val dao: CountryDao,
    private val countriesResourceManager: CountriesResourceManager
) : CountriesRepository {

    override suspend fun loadAllCountries(): NetworkResult<List<Country>> {
        return try {
            val countryDtoList = apiService.getAllCountries()
            for (countryDto in countryDtoList) {
                val countryDBModel = mapper.mapDtoToDBModel(countryDto)
                countryDBModel?.let {
                    dao.insertCountry(it)
                } ?: Log.e(LOG_TAG, "Null common name for dto: $countryDto")
            }
            val countriesFromDb = dao.getCountries().map {
                mapper.mapDBModelToDomainEntity(it)
            }
            NetworkResult.success(countriesFromDb)
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
            val countriesFromDb = dao.getCountries()
            val result = if (countriesFromDb.isNotEmpty()) {
                countriesFromDb.map {
                    mapper.mapDBModelToDomainEntity(it)
                }
            } else null
            val msg = if (e is UnknownHostException) {
                countriesResourceManager.returnOfflineErrorString()
            } else {
                countriesResourceManager.returnNetworkErrorString()
            }
            NetworkResult.error(result, msg)
        }
    }

    override suspend fun getCountryByName(commonName: String): Country? {
        return dao.getCountryByName(commonName)?.let { mapper.mapDBModelToDomainEntity(it) }
    }
}