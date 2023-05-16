package com.panasetskaia.countriesscroller.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.application.CountriesScrollerApp
import com.panasetskaia.countriesscroller.di.viewmodel.ViewModelFactory
import com.panasetskaia.countriesscroller.presentation.all_countries_screen.AllCountriesViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val component by lazy {
        (application as CountriesScrollerApp).component
    }

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AllCountriesViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel
    }
}