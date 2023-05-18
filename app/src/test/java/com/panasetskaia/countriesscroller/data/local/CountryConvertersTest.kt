package com.panasetskaia.countriesscroller.data.local

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CountryConvertersTest {

    private lateinit var SUT: CountryConverters

    @Before
    fun setUp() {
        SUT = CountryConverters()
    }

    @Test
    fun doubleConverting_returnsInitialValue() {
        val initialList = listOf("Spanish", "Danish")

        val convertedToString = SUT.listToString(initialList)
        val convertedBackToList = SUT.stringToList(convertedToString)

        assertThat(convertedBackToList).isEqualTo(initialList)
    }
}