package com.panasetskaia.countriesscroller.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FlagsContainerDto(
    @SerializedName("png")
    @Expose
    val flagPngUrl: String?
)
