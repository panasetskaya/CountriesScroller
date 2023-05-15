package com.panasetskaia.countriesscroller.di.data

import android.app.Application
import com.panasetskaia.countriesscroller.data.local.CountryDao
import com.panasetskaia.countriesscroller.data.local.CountryDatabase
import com.panasetskaia.countriesscroller.data.network.ApiFactory
import com.panasetskaia.countriesscroller.data.network.ApiService
import com.panasetskaia.countriesscroller.data.repo.CountriesRepositoryImpl
import com.panasetskaia.countriesscroller.di.CountriesScrollerScope
import com.panasetskaia.countriesscroller.domain.CountriesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @CountriesScrollerScope
    @Binds
    fun bindRepo(repoImpl: CountriesRepositoryImpl): CountriesRepository

    companion object {

        @CountriesScrollerScope
        @Provides
        fun provideApi(): ApiService {
            return ApiFactory.apiService
        }

        @CountriesScrollerScope
        @Provides
        fun provideDao(application: Application): CountryDao {
            return CountryDatabase.getInstance(application).countryDao()
        }
    }

}