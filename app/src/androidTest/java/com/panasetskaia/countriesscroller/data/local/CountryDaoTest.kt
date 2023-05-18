package com.panasetskaia.countriesscroller.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CountryDaoTest {

    private lateinit var database: CountryDatabase
    private lateinit var SUT: CountryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CountryDatabase::class.java)
            .build()
        SUT = database.countryDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertCountry() {
        runTest {
            val countryDb = CountryDBModel(
                "name",
                null,
                null,
                listOf(),
                null,
                null,
                null
            )

            SUT.insertCountry(countryDb)
            val countries = SUT.getCountries()

            assertThat(countryDb).isIn(countries)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun nameExists_returnsCountryDBModel() {
        runTest {
            val countryDb = CountryDBModel(
                "name",
                null,
                null,
                listOf(),
                null,
                null,
                null
            )

            SUT.insertCountry(countryDb)
            val result = SUT.getCountryByName(countryDb.commonName)

            assertThat(result).isNotNull()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun noSuchName_returnsNull() {
        runTest {
            val countryDb = CountryDBModel(
                "name",
                null,
                null,
                listOf(),
                null,
                null,
                null
            )

            val result = SUT.getCountryByName(countryDb.commonName)

            assertThat(result).isNull()
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }
}