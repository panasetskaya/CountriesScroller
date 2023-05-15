package com.panasetskaia.countriesscroller.utils

import androidx.fragment.app.Fragment
import com.panasetskaia.countriesscroller.application.CountriesScrollerApp
import com.panasetskaia.countriesscroller.di.AppComponent

object Constants {
    const val LOG_TAG = "CountriesScrollerLog"
}

fun Fragment.getAppComponent(): AppComponent =
    (requireActivity().application as CountriesScrollerApp).component