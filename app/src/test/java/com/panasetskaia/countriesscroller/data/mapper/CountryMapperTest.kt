package com.panasetskaia.countriesscroller.data.mapper

import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import com.panasetskaia.countriesscroller.data.network.model.NamesContainerDto
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.panasetskaia.countriesscroller.domain.Country

class CountryMapperTest {

    private lateinit var mapper: CountryMapper

    @Before
    fun setUp() {
        mapper = CountryMapper()
    }

    @Test
    fun commonNameIsNull_returnsNull() {
        val dtoModel = CountryDto(
            NamesContainerDto(null, "official"),
            "subregion",
            null,
            null,
            null,
            null
        )
        val result = mapper.mapDtoToDomain(dtoModel)
        assertThat(result).isNull()
    }

    @Test
    fun commonNameIsNotNull_returnsCountry() {
        val dtoModel = CountryDto(
            NamesContainerDto("common", "official"),
            "subregion",
            null,
            null,
            null,
            null
        )
        val result = mapper.mapDtoToDomain(dtoModel)
        assertThat(result).isEqualTo(Country(
            "common",
            "official",
            "subregion",
            listOf(),
            null,
            null,
            null
        ))
    }

    @Test
    fun languagesJsonObjectIsNull_returnsEmptyList() {
        val dtoModel = CountryDto(
            NamesContainerDto("common", "official"),
            "subregion",
            null,
            null,
            null,
            null
        )
        val country = mapper.mapDtoToDomain(dtoModel)
        country?.let {
            assertThat(it.languages).isEqualTo(listOf<String>())
        }
    }
}