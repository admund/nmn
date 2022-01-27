package me.admund.nmn.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.admund.nmn.R
import me.admund.nmn.databinding.ViewCountryItemBinding
import me.admund.nmn.domain.Country
import kotlin.math.roundToInt

class CountriesAdapter(
    val onClickStarListener: (Long) -> Unit
) : ListAdapter<Country, CountryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ViewCountryItemBinding.inflate(layoutInflater, parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            val context = root.context
            nameTextView.text = item.name
            vaccinatedTextView.text = numberAndPercentText(
                context = context,
                value = item.peopleVaccinated,
                population = item.population,
                isFullyText = true
            )
            partiallyVaccinatedTextView.text = numberAndPercentText(
                context = context,
                value = item.peoplePartiallyVaccinated,
                population = item.population,
                isFullyText = false
            )
            favoriteImageView.setImageResource(
                when (item.isFavorite) {
                    true -> android.R.drawable.star_big_on
                    false -> android.R.drawable.star_big_off
                }
            )
            favoriteImageView.setOnClickListener { onClickStarListener(item.uid) }
        }
    }

    private fun numberAndPercentText(
        context: Context,
        value: Long,
        population: Long,
        isFullyText: Boolean
    ): String {
        val prefix = context.getString(
            when (isFullyText) {
                true -> R.string.full_vaccinated_text
                false -> R.string.partially_vaccinated_text
            }
        )
        return if (value == 0L || population == 0L) {
            prefix + context.getText(R.string.no_data)
        } else {
            prefix + String.format(
                context.getString(R.string.number_and_percent),
                value,
                getPercent(value, population)
            )
        }
    }

    private fun getPercent(value: Long, population: Long): Int {
        if (value == 0L) return 0
        return (value.toDouble() / population * 100).roundToInt()
    }
}

class CountryViewHolder(val binding: ViewCountryItemBinding) : RecyclerView.ViewHolder(binding.root)

private class DiffCallback : DiffUtil.ItemCallback<Country>() {

    override fun areItemsTheSame(oldItem: Country, newItem: Country) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: Country, newItem: Country) =
        oldItem == newItem
}
