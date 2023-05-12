package com.panasetskaia.countriesscroller.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NamesContainerDto(
    @SerializedName("common")
    @Expose
    val commonName: String,
    @SerializedName("official")
    @Expose
    val officialName: String
)
