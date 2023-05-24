package com.panasetskaia.countriesscroller.data.repo

import com.google.common.truth.Truth.assertThat
import com.panasetskaia.countriesscroller.data.local.CountryDao
import com.panasetskaia.countriesscroller.data.mapper.CountryMapper
import com.panasetskaia.countriesscroller.data.network.ApiService
import com.panasetskaia.countriesscroller.domain.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.json.JSONException
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.net.UnknownHostException

class CountriesRepositoryImplTest {

    private lateinit var SUT: CountriesRepositoryImpl
    private lateinit var apiService: ApiService
    private lateinit var mapper: CountryMapper
    private lateinit var dao: CountryDao
    private lateinit var resourceManager: CountriesResourceManager

    @Before
    fun setUp() {
        mapper = CountryMapper()
        resourceManager = mock(CountriesResourceManager::class.java)
        apiService = mock(ApiService::class.java)
        dao = mock(CountryDao::class.java)
        SUT = CountriesRepositoryImpl(
            apiService,
            mapper,
            dao,
            resourceManager
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGoodResponse_returnsNetworkResultSuccess() {
        runTest {
            whenever(apiService.getAllCountries()).thenReturn(arrayListOf())
            whenever(dao.getCountries()).thenReturn(listOf())

            val result = SUT.loadAllCountries()

            assertThat(result.status).isEqualTo(Status.SUCCESS)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getsUnknownHostException_returnsOfflineMessage() {
        runTest {
            val offlineString = "offline"
            whenever(apiService.getAllCountries()).thenThrow(UnknownHostException())
            whenever(dao.getCountries()).thenReturn(listOf())
            whenever(resourceManager.returnOfflineErrorString()).thenReturn(offlineString)


            val result = SUT.loadAllCountries()

            assertThat(result.status).isEqualTo(Status.ERROR)
            assertThat(result.msg).isEqualTo(offlineString)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getsAnotherException_returnsNetworkErrorMessage() {
        runTest {
            val network = "network"
            whenever(apiService.getAllCountries()).thenThrow(JSONException(""))
            whenever(dao.getCountries()).thenReturn(listOf())
            whenever(resourceManager.returnNetworkErrorString()).thenReturn(network)

            val result = SUT.loadAllCountries()

            assertThat(result.status).isEqualTo(Status.ERROR)
            assertThat(result.msg).isEqualTo(network)
        }
    }
}