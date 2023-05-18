package com.panasetskaia.countriesscroller.data.mapper

import android.util.Log

import com.panasetskaia.countriesscroller.data.local.CountryDBModel
import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.utils.Constants
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CountryMapper @Inject constructor() {

    fun mapDtoToDBModel(dtoModel: CountryDto): CountryDBModel? {
        val languages = parseLanguages(dtoModel)
        val nameContainer = dtoModel.name
        if (nameContainer == null) return null else {
            val commonName = nameContainer.commonName ?: return null
            val officialName = dtoModel.name.officialName
            val capital = dtoModel.capital?.get(0)
            val flagUrl = dtoModel.flags?.flagPngUrl
            return CountryDBModel(
                commonName,
                officialName,
                dtoModel.subregion,
                languages,
                capital,
                dtoModel.population,
                flagUrl
            )
        }
    }

    fun mapDBModelToDomainEntity(dbModel: CountryDBModel): Country {
        return Country(
            dbModel.commonName,
            dbModel.officialName,
            dbModel.subregion,
            dbModel.languages,
            dbModel.capital,
            dbModel.population,
            dbModel.flagUrl
        )
    }

    private fun parseLanguages(dtoModel: CountryDto): List<String> {
        val result = mutableListOf<String>()
        val languagesJsonObj = dtoModel.languages ?: return result
        val keys = languagesJsonObj.keys
        for (key in keys) {
            try {
                val language = Json.encodeToString(languagesJsonObj[key]).trim('"')
                result.add(language)
            } catch (e: Exception) {
                Log.e(
                    Constants.LOG_TAG,
                    "Error occurred when parsing languagesJsonObject: ${e.message}"
                )
            }
        }
        return result
    }
}