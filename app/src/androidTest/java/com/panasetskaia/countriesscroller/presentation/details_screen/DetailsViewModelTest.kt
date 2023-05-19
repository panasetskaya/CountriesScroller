package com.panasetskaia.countriesscroller.presentation.details_screen

import com.google.common.truth.Truth.assertThat
import com.panasetskaia.countriesscroller.data.repo.FakeRepository
import com.panasetskaia.countriesscroller.domain.Country
import com.panasetskaia.countriesscroller.domain.GetCountryByNameUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest {

    private lateinit var SUT: DetailsViewModel
    private lateinit var getCountryByNameUseCase: GetCountryByNameUseCase
    private lateinit var repo: FakeRepository

    @Before
    fun setUp() {
        repo = FakeRepository()
        getCountryByNameUseCase = GetCountryByNameUseCase(repo)
        SUT = DetailsViewModel(getCountryByNameUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_NameExists_Expect_CountryWithThisName() {
        val name = "Brazil"
        repo.shouldReturnCountry = true
        runTest {
            SUT.getCountryByName(name)
            val result = SUT.country.first()
            assertThat(result.commonName).isEqualTo(name)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_NoSuchName_Expect_NoNewEmits() {
        val name = "Brazil"
        repo.shouldReturnCountry = false
        runTest {
            SUT.getCountryByName(name)
            val result = withTimeoutOrNull(1000) { SUT.country.first() }
            assertThat(result).isNull()
        }
    }

    @Test
    fun when_DetailsAreNull_Expect_False() {
        val country = Country("", "", null, listOf(), null, 0, "")
        val result = SUT.detailsAreNotNull(country)
        assertThat(result).isFalse()
    }

    @Test
    fun when_DetailsAreEmpty_Expect_False() {
        val country = Country("", "", "", listOf(), "", 0, "")
        val result = SUT.detailsAreNotNull(country)
        assertThat(result).isFalse()
    }

    @Test
    fun when_DetailsAreNotEmpty_Expect_True() {
        val country = Country("", "", "sub", listOf(), "cap", 12, "")
        val result = SUT.detailsAreNotNull(country)
        assertThat(result).isTrue()
    }
}