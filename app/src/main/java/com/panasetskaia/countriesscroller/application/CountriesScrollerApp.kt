package com.panasetskaia.countriesscroller.application

import android.app.Application
import com.panasetskaia.countriesscroller.di.DaggerAppComponent

open class CountriesScrollerApp: Application() {

    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }

}