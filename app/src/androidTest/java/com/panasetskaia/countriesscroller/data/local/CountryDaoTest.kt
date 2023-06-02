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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun conflictingValues_ReplacesCorrectly() {
        runTest {
            val name = "name"
            val region = "region"
            val initialValue = CountryDBModel(
                name,
                null,
                null,
                listOf(),
                null,
                null,
                null
            )
            val conflictingValue =  CountryDBModel(
                name,
            null,
                region,
            listOf(),
            null,
            null,
            null
        )

            SUT.insertCountry(initialValue)
            SUT.insertCountry(conflictingValue)
            val result = SUT.getCountries().filter {
                it.commonName==name
            }

            assertThat(result.size).isEqualTo(1)
            assertThat(result[0].subregion).isEqualTo(region)

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getsCountries_returnsInRightOrder() {
        runTest {
            val firstValue = CountryDBModel(
                "ssss",
                null,
                null,
                listOf(),
                null,
                null,
                null
            )
            val secondValue = CountryDBModel(
                "aaa",
                null,
                null,
                listOf(),
                null,
                null,
                null
            )

            SUT.insertCountry(firstValue)
            SUT.insertCountry(secondValue)
            val result = SUT.getCountries()

            assertThat(result[0]).isEqualTo(secondValue)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

}