package com.panasetskaia.countriesscroller.presentation.base

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<mbinding : ViewBinding, mviewModel: BaseViewModel> : Fragment() {
}