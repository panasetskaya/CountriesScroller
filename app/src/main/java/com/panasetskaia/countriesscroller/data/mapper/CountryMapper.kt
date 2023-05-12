package com.panasetskaia.countriesscroller.data.mapper

import com.google.gson.Gson
import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import com.panasetskaia.countriesscroller.domain.Country

class CountryMapper {

    fun mapDtoToDomain(dtoModel: CountryDto): Country {
        val languages = parseLanguages(dtoModel)
        val commonName = dtoModel.name?.commonName
        val officialName = dtoModel.name?.officialName
        val capital = dtoModel.capital?.get(0)
        val flagUrl = dtoModel.flags?.flagPngUrl
        return Country(
            commonName,
            officialName,
            dtoModel.subregion,
            languages,
            capital,
            dtoModel.population,
            flagUrl
        )
    }

    private fun parseLanguages(dtoModel: CountryDto): List<String> {
        val result = mutableListOf<String>()
        val languagesJsonObj = dtoModel.languages ?: return result
        val keys = languagesJsonObj.keySet()
        for (key in keys) {
            val language =
                Gson().fromJson(languagesJsonObj.getAsJsonPrimitive(key), String::class.java)
            result.add(language)
        }
        return result
    }
}