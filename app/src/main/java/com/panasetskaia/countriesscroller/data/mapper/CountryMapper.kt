package com.panasetskaia.countriesscroller.data.mapper

import android.util.Log
import com.google.gson.Gson
import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.utils.Constants
import javax.inject.Inject

class CountryMapper @Inject constructor() {

    /**
     * Returns a Country object if the dtoModel's commonName is not null otherwise returns null
     */
    fun mapDtoToDomain(dtoModel: CountryDto): Country? {
        val languages = parseLanguages(dtoModel)
        val nameContainer = dtoModel.name
        if (nameContainer==null) return null else {
            val commonName = nameContainer.commonName ?: return null
            val officialName = dtoModel.name.officialName
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
    }

    private fun parseLanguages(dtoModel: CountryDto): List<String> {
        val result = mutableListOf<String>()
        val languagesJsonObj = dtoModel.languages ?: return result
        val keys = languagesJsonObj.keySet()
        for (key in keys) {
            try {
                val language =
                    Gson().fromJson(languagesJsonObj.getAsJsonPrimitive(key), String::class.java)
                result.add(language)
            } catch (e:Exception) {
                Log.e(Constants.LOG_TAG,"Error occurred when parsing languagesJsonObject: ${e.message}")
            }
        }
        return result
    }
}