package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import com.google.common.truth.Truth.assertThat
import com.panasetskaia.countriesscroller.data.repo.FakeRepository
import com.panasetskaia.countriesscroller.domain.LoadAllCountriesUseCase
import com.panasetskaia.countriesscroller.domain.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Test

class AllCountriesViewModelTest {

    private lateinit var SUT: AllCountriesViewModel
    private lateinit var loadAllCountriesUseCase: LoadAllCountriesUseCase
    private lateinit var repo: FakeRepository

    @Before
    fun setUp() {
        repo = FakeRepository()
        loadAllCountriesUseCase = LoadAllCountriesUseCase(repo)
        SUT = AllCountriesViewModel(loadAllCountriesUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_ResponseIsError_Expect_EmitsErrorWithList() {
        repo.shouldReturnError = true
        runTest {
            val result = SUT.countriesList.first()
            assertThat(result.status).isEqualTo(Status.ERROR)
            assertThat(result.data).isNotNull()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_ResponseIsSuccess_Expect_EmitsSuccess() {
        repo.shouldReturnError = false
        runTest {
            val result = SUT.countriesList.first()
            assertThat(result.status).isEqualTo(Status.SUCCESS)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_ApplyFilter_Expect_FilterOptionsChanged() {
        runTest {
            val initialOptions = SUT.filterOptions.first()
            SUT.changeFiltering("asia", 1)
            val result = SUT.filterOptions.first()
            assertThat(result).isNotEqualTo(initialOptions)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun when_FilterCanceled_Expect_EmitNullSubregion() {
        runTest {
            SUT.changeFiltering("asia")
            SUT.cancelFiltering()
            val result = withTimeout(1000) { SUT.filterOptions.first() }
            assertThat(result.subRegion).isNull()
        }
    }
}