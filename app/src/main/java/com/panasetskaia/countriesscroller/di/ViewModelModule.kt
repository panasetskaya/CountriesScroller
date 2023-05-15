package com.panasetskaia.countriesscroller.di

import androidx.lifecycle.ViewModel
import com.panasetskaia.countriesscroller.presentation.AllCountriesViewModel
import com.panasetskaia.countriesscroller.presentation.details_screen.DetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AllCountriesViewModel::class)
    fun bindAllCountriesViewModel(impl: AllCountriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    fun bindDetailsViewModel(impl: DetailsViewModel): ViewModel
}