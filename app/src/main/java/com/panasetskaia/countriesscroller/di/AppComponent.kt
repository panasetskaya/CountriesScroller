package com.panasetskaia.countriesscroller.di

import android.app.Application
import android.telecom.Call.Details
import com.panasetskaia.countriesscroller.presentation.all_countries_screen.AllCountriesFragment
import com.panasetskaia.countriesscroller.presentation.MainActivity
import com.panasetskaia.countriesscroller.presentation.details_screen.DetailsFragment
import dagger.BindsInstance
import dagger.Component

@CountriesScrollerScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: AllCountriesFragment)

    fun inject(fragment: DetailsFragment)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }

}