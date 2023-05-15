package com.panasetskaia.countriesscroller.data.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CountryDto(

    @SerialName("name")
    val name: NamesContainerDto? = null,

    @SerialName("subregion")
    val subregion: String? = null,

    @SerialName("languages")
    val languages: JsonObject? = null,

    @SerialName("capital")
    val capital: List<String>? = null,

    @SerialName("population")
    val population: Int? = null,

    @SerialName("flags")
    val flags: FlagsContainerDto? = null
)