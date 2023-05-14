package com.panasetskaia.countriesscroller.presentation.allCountriesScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.ItemCountryBinding
import com.panasetskaia.countriesscroller.domain.Country

class CountriesListAdapter(private val onItemClickedListener: (Country) -> Unit) :
    ListAdapter<Country, CountriesListAdapter.CountriesViewHolder>(CountriesDiffUtil()) {
    class CountriesViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val binding = ItemCountryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.binding
        binding.root.setOnLongClickListener {
            onItemClickedListener(item)
            true
        }
        with(binding) {
            textViewCountryName.text = item.commonName
            Glide.with(root.context).load(item.flagUrl).circleCrop()
                .placeholder(R.drawable.flag_placeholder)
                .into(imageViewSmallFlag)
        }
    }
}