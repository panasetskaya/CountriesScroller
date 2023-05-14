package com.panasetskaia.countriesscroller.data.network.model

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("name")
    @Expose
    val name: NamesContainerDto?,
    @SerializedName("subregion")
    @Expose
    val subregion: String?,
    @SerializedName("languages")
    @Expose
    val languages: JsonObject?,
    @SerializedName("capital")
    @Expose
    val capital: List<String>?,
    @SerializedName("population")
    @Expose
    val population: Int?,
    @SerializedName("flags")
    @Expose
    val flags: FlagsContainerDto?
)