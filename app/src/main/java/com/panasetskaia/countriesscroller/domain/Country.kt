package com.panasetskaia.countriesscroller.domain

data class Country(
    val commonName: String?,
    val officialName: String?,
    val subregion: String?,
    val languages: List<String>,
    val capital: String?,
    val population: Long?,
    val flagUrl: String?
    )
