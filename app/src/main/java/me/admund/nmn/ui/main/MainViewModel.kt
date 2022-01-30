package me.admund.nmn.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.admund.nmn.domain.Country
import me.admund.nmn.domain.CountriesRepository

class MainViewModel(
    private val countriesRepository: CountriesRepository,
    private val testCoroutineScope: CoroutineScope? = null
) : ViewModel() {

    private val countriesStateFlow = MutableStateFlow<List<Country>>(emptyList())
    private val errorsStateFlow = MutableStateFlow<MainViewModelError>(MainViewModelError.None)
    private var showOnlyFavorite = false
    private var cachedCountriesList: List<Country> = emptyList()

    init {
        countriesRepository.countries().onEach { list ->
            cachedCountriesList = list
            countriesStateFlow.value = filteredCountriesList(list)
        }.launchIn(scope)
        countriesRepository.fetchCountries(scope) {
            errorsStateFlow.value = MainViewModelError.FetchCountriesIOException
        }
    }

    fun countries(): Flow<List<Country>> = countriesStateFlow

    fun errors(): Flow<MainViewModelError> = errorsStateFlow

    fun swapCountryIsFavorite(uid: Long) {
        countriesRepository.swapCountryIsFavorite(scope, uid)
    }

    fun showOnlyFavorite(showOnlyFavorite: Boolean) {
        this.showOnlyFavorite = showOnlyFavorite
        countriesStateFlow.value = filteredCountriesList(cachedCountriesList)
    }

    private fun filteredCountriesList(list: List<Country>) = when (showOnlyFavorite) {
        true -> list.filter { it.isFavorite }
        false -> list
    }.let { return@let if (it.isEmpty()) list else it }

    private val scope: CoroutineScope
        get() = testCoroutineScope ?: viewModelScope
}

sealed class MainViewModelError {
    object None : MainViewModelError()
    object FetchCountriesIOException : MainViewModelError()
}
