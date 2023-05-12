package com.panasetskaia.countriesscroller.di

import android.app.Application
import com.panasetskaia.countriesscroller.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@CountriesScrollerScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }

}