package com.panasetskaia.countriesscroller.data.local

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class CountryConvertersTest {

    private lateinit var converter: CountryConverters

    @Before
    fun setUp() {
        converter = CountryConverters()
    }

    @Test
    fun doubleConvertingInitialValue_returnsInitialValue() {
        val initialList = listOf("Spanish", "Danish")
        val convertedToString = converter.listToString(initialList)
        val convertedBackToList = converter.stringToList(convertedToString)
        assertThat(convertedBackToList).isEqualTo(initialList)
    }
}