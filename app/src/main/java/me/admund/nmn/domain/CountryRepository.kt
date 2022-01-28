package me.admund.nmn.domain

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.admund.nmn.data.api.VaccinesApi
import me.admund.nmn.data.db.CountriesDao
import me.admund.nmn.data.db.CountryDbEntity
import java.io.IOException

interface CountryRepository {
    fun countries(): Flow<List<Country>>
    fun fetchCountries(coroutineScope: CoroutineScope, onIoExceptionListener: (Throwable) -> Unit)
    fun swapCountryFavoriteStatus(coroutineScope: CoroutineScope, uid: Long)
}

class CountryRepositoryImpl(
    private val vaccinesApi: VaccinesApi,
    private val countriesDao: CountriesDao
) : CountryRepository {
    private var countryList = emptyList<Country>()

    override fun countries(): Flow<List<Country>> = countriesDao.queryAllCountries().map { list ->
        list.map { it.toCountry() }.also {
            countryList = it
        }
    }

    override fun fetchCountries(
        coroutineScope: CoroutineScope,
        onIoExceptionListener: (Throwable) -> Unit
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("NMN", "CoroutineExceptionHandler got $exception")
            when (exception) {
                is IOException -> onIoExceptionListener(exception)
                else -> throw exception
            }
        }

        coroutineScope.launch(handler) {
            val result = mutableListOf<CountryDbEntity>()
            vaccinesApi.vaccines().onEach { countryEntry ->
                val countryName = countryEntry.key
                val vaccineEntity = countryEntry.value["All"]
                val uid = vaccineEntity?.administered ?: -1
                result.add(
                    CountryDbEntity(
                        uid = uid,
                        name = countryName,
                        isFavorite = isFavorite(uid),
                        population = vaccineEntity?.population ?: NO_DATA,
                        peopleVaccinated = vaccineEntity?.people_vaccinated ?: NO_DATA,
                        peoplePartiallyVaccinated = vaccineEntity?.people_partially_vaccinated
                            ?: NO_DATA
                    )
                )
            }
            checkIfAdministeredCanBeUid(result)
            if (countriesDao.updateAll(result) == 0) {
                countriesDao.insertAll(result)
            }
        }
    }

    override fun swapCountryFavoriteStatus(coroutineScope: CoroutineScope, uid: Long) {
        countryList.find { country -> country.uid == uid }
            ?.let { country ->
                coroutineScope.launch {
                    countriesDao.update(
                        CountryDbEntity.fromCountry(
                            country.copy(isFavorite = !country.isFavorite)
                        )
                    )
                }
            }
    }

    private fun isFavorite(uid: Long) = countryList.find { it.uid == uid }?.isFavorite ?: false

    private fun checkIfAdministeredCanBeUid(list: List<CountryDbEntity>) {
        val checkSet = mutableSetOf<Long>()
        list.onEach { country ->
            if (checkSet.contains(country.uid)) {
                Log.e("NMN", "'uid' duplicates: ${country.uid}")
            } else {
                checkSet.add(country.uid)
            }
        }
    }

    companion object {
        const val NO_DATA = -1L
    }
}
