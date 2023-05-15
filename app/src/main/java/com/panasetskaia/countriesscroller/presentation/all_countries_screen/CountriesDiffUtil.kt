package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import androidx.recyclerview.widget.DiffUtil
import com.panasetskaia.countriesscroller.domain.Country

class CountriesDiffUtil: DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.commonName==newItem.commonName
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem==newItem
    }
}
