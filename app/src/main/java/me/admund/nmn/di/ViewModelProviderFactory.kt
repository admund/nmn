package me.admund.nmn.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.admund.nmn.domain.CountryRepository
import me.admund.nmn.ui.main.MainViewModel

class ViewModelProviderFactory(
    private val countryRepository: CountryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(countryRepository) as T
            else -> throw NoViewModelException(modelClass.name)
        }
    }
}

class NoViewModelException(viewModelName: String) :
    RuntimeException("No ViewModel: $viewModelName")

