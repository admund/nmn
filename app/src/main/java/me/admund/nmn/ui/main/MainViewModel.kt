package me.admund.nmn.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.admund.nmn.domain.Country
import me.admund.nmn.domain.CountryRepository

class MainViewModel(
    private val countryRepository: CountryRepository
) : ViewModel() {

    private val countriesStateFlow = MutableStateFlow<List<Country>>(emptyList())
    private val errorsStateFlow = MutableStateFlow<MainViewModelError>(MainViewModelError.None)
    private var showOnlyFavorite = false
    private var cachedList: List<Country> = emptyList()

    init {
        countryRepository.countries().onEach { list ->
            cachedList = list
            countriesStateFlow.value = filteredList(list)
        }.launchIn(viewModelScope)
        countryRepository.fetchCountries(viewModelScope) {
            errorsStateFlow.value = MainViewModelError.FetchCountriesIOException
        }
    }

    fun countries(): Flow<List<Country>> = countriesStateFlow

    fun errors(): Flow<MainViewModelError> = errorsStateFlow

    fun updateCountryFavoriteStatus(uid: Long) {
        countryRepository.updateCountryFavoriteStatus(viewModelScope, uid)
    }

    fun showOnlyFavorite(showOnlyFavorite: Boolean) {
        Log.e("ZXC", "showOnlyFavorite: $showOnlyFavorite")
        this.showOnlyFavorite = showOnlyFavorite
        countriesStateFlow.value = filteredList(cachedList)
    }

    private fun filteredList(list: List<Country>) = when (showOnlyFavorite) {
        true -> list.filter { it.isFavorite }
        false -> list
    }.let { return@let if (it.isEmpty()) list else it }
}

sealed class MainViewModelError {
    object None : MainViewModelError()
    object FetchCountriesIOException : MainViewModelError()
}
