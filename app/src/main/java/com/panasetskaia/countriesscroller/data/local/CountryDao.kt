package com.panasetskaia.countriesscroller.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryDao {

    @Query("SELECT * FROM countrydbmodel")
    suspend fun getCountries(): List<CountryDBModel>

    @Query("SELECT * FROM countrydbmodel WHERE commonName == :commonName LIMIT 1")
    suspend fun getCountryByName(commonName: String): CountryDBModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(countryDBModel: CountryDBModel)

}