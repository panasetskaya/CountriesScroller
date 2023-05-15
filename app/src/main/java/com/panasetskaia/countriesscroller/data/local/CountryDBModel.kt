package com.panasetskaia.countriesscroller.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class CountryDBModel(
    @PrimaryKey
    val commonName: String,
    val officialName: String?,
    val subregion: String?,
    val languages: List<String>,
    val capital: String?,
    val population: Int?,
    val flagUrl: String?
)
