package com.panasetskaia.countriesscroller.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.panasetskaia.countriesscroller.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        testLoading()
    }

    private fun testLoading() {
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}