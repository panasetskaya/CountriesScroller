package com.panasetskaia.countriesscroller.presentation.all_countries_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.panasetskaia.countriesscroller.R
import com.panasetskaia.countriesscroller.databinding.ItemCountryBinding
import com.panasetskaia.countriesscroller.domain.Country

class CountriesListAdapter:
    ListAdapter<Country, CountriesListAdapter.CountriesViewHolder>(CountriesDiffUtil()) {
    class CountriesViewHolder(val binding: ItemCountryBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onItemClickedListener: ((Country) -> Unit)? = null

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
        binding.root.setOnClickListener {
            onItemClickedListener?.invoke(item)
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