package com.panasetskaia.countriesscroller.data.mapper


import com.google.common.truth.Truth.assertThat
import com.panasetskaia.countriesscroller.data.local.CountryDBModel
import com.panasetskaia.countriesscroller.data.network.model.CountryDto
import com.panasetskaia.countriesscroller.data.network.model.NamesContainerDto
import org.junit.Before
import org.junit.Test

class CountryMapperTest {

    private lateinit var SUT: CountryMapper

    @Before
    fun setUp() {
        SUT = CountryMapper()
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

        val result = SUT.mapDtoToDBModel(dtoModel)

        assertThat(result).isNull()
    }

    @Test
    fun commonNameIsNotNull_returnsCountryDBModel() {
        val dtoModel = CountryDto(
            NamesContainerDto("common", "official"),
            "subregion",
            null,
            null,
            null,
            null
        )

        val result = SUT.mapDtoToDBModel(dtoModel)

        assertThat(result).isEqualTo(
            CountryDBModel(
                "common",
                "official",
                "subregion",
                listOf(),
                null,
                null,
                null
            )
        )
    }

    @Test
    fun languagesJsonObjectIsNull_returnsEmptyListAsLanguages() {
        val dtoModel = CountryDto(
            NamesContainerDto("common", "official"),
            "subregion",
            null,
            null,
            null,
            null
        )

        val country = SUT.mapDtoToDBModel(dtoModel)

        country?.let {
            assertThat(it.languages).isEqualTo(listOf<String>())
        }
    }
}