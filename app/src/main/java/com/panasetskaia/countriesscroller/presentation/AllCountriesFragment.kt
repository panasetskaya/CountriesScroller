package com.panasetskaia.countriesscroller.presentation

import android.content.Context
import android.os.Bundle
import com.panasetskaia.countriesscroller.databinding.FragmentAllCountriesBinding
import com.panasetskaia.countriesscroller.presentation.base.BaseFragment
import com.panasetskaia.countriesscroller.utils.getAppComponent
import javax.inject.Inject

class AllCountriesFragment :
    BaseFragment<FragmentAllCountriesBinding, MainViewModel>(FragmentAllCountriesBinding::inflate) {

    @Inject
    override lateinit var viewModel: MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAppComponent().inject(this)
    }


    override fun onReady(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AllCountriesFragment()
    }
}