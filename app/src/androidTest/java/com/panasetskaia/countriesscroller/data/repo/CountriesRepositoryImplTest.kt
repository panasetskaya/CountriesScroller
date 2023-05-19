package com.panasetskaia.countriesscroller.data.repo

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.panasetskaia.countriesscroller.R
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

    @Before
    fun setUp() {
        mapper = CountryMapper()
        apiService = mock(ApiService::class.java)
        dao = mock(CountryDao::class.java)
        SUT = CountriesRepositoryImpl(
            apiService,
            mapper,
            dao,
            ApplicationProvider.getApplicationContext()
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getGoodResponse_returnsNetworkResultSuccess() {
        runTest {
            whenever(apiService.getAllCountries()).thenReturn(arrayListOf())
            whenever (dao.getCountries()).thenReturn(listOf() )

            val result = SUT.loadAllCountries()

            assertThat(result.status).isEqualTo(Status.SUCCESS)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getsUnknownHostException_returnsOfflineMessage() {
        runTest {
            whenever(apiService.getAllCountries()).thenThrow(UnknownHostException())
            whenever (dao.getCountries()).thenReturn(listOf() )
            val context = ApplicationProvider.getApplicationContext<Context>()

            val result = SUT.loadAllCountries()
            assertThat(result.status).isEqualTo(Status.ERROR)
            assertThat(result.msg).isEqualTo(context.getString(R.string.offline_error))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getsAnotherException_returnsNetworkErrorMessage() {
        runTest {
            whenever(apiService.getAllCountries()).thenThrow(JSONException(""))
            whenever (dao.getCountries()).thenReturn(listOf() )
            val context = ApplicationProvider.getApplicationContext<Context>()

            val result = SUT.loadAllCountries()

            assertThat(result.status).isEqualTo(Status.ERROR)
            assertThat(result.msg).isEqualTo(context.getString(R.string.network_error))
        }
    }
}