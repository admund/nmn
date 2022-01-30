package me.admund.nmn.domain

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import me.admund.nmn.data.api.VaccineApiEntity
import me.admund.nmn.data.api.VaccinesApi
import me.admund.nmn.data.db.CountriesDao
import me.admund.nmn.data.db.CountryDbEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CountriesRepositoryTest {

    private val testScope = TestCoroutineScope()
    private val vaccinesApi = mockk<VaccinesApi>()
    private val countriesDao = mockk<CountriesDao>()
    private val onIoExceptionListener = mockk<(Throwable) -> Unit>()

    private val tested: CountriesRepository = CountriesRepositoryImpl(vaccinesApi, countriesDao)

    @Test
    fun `test if insertAll() is not invoked when some rows was updated`() {
        coEvery { vaccinesApi.vaccines() } returns vaccinesApiMap
        coEvery { countriesDao.updateAll(any()) } returns 1
        coEvery { countriesDao.insertAll(any()) } returns Unit

        tested.fetchCountries(testScope) {
            assert(false)
        }

        coVerify(exactly = 1) { countriesDao.updateAll(countryDbEntityListAllNoFavorite) }
        coVerify(exactly = 0) { countriesDao.insertAll(any()) }
    }

    @Test
    fun `test if insertAll() is invoked when zero rows was updated`() {
        coEvery { vaccinesApi.vaccines() } returns vaccinesApiMap
        coEvery { countriesDao.updateAll(any()) } returns 0
        coEvery { countriesDao.insertAll(any()) } returns Unit

        tested.fetchCountries(testScope) {
            assert(false)
        }

        coVerify(exactly = 1) { countriesDao.updateAll(countryDbEntityListAllNoFavorite) }
        coVerify(exactly = 1) { countriesDao.insertAll(countryDbEntityListAllNoFavorite) }
    }

    @Test
    fun `test if onIoExceptionListener will be invoked when vaccines will throw IOException`() {
        coEvery { vaccinesApi.vaccines() } throws IOException()

        tested.fetchCountries(testScope, onIoExceptionListener)

        coVerify { onIoExceptionListener(any()) }
        coVerify { countriesDao wasNot Called }
    }

    @Test
    fun `test if onIoExceptionListener will not be invoked when vaccines will throw other exception`() {
        coEvery { vaccinesApi.vaccines() } throws Exception()

        tested.fetchCountries(testScope, onIoExceptionListener)

        coVerify { onIoExceptionListener wasNot Called }
        coVerify { countriesDao wasNot Called }
    }

    @Test
    fun `test if list of Country will be same as returned from database`() = runBlockingTest {
        mockQueryAllCountries(countryDbEntityList)

        tested.countries().test {
            assertEquals(countryList, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `test if swapCountryFavoriteStatus will invoke proper update on countriesDao`() {
        mockQueryAllCountries(countryDbEntityList)

        tested.countries().launchIn(testScope)
        tested.swapCountryIsFavorite(testScope, 3)

        coVerify(exactly = 1) { countriesDao.update(favoriteUid3CountryDbEntity) }
    }

    private fun mockQueryAllCountries(map: List<CountryDbEntity>) {
        every { countriesDao.queryAllCountries() } returns flow { emit(map) }
    }

    private val vaccinesApiMap = mapOf(
        "CNT1" to mapOf("All" to VaccineApiEntity(1, 10, 1, 2)),
        "CNT2" to mapOf("All" to VaccineApiEntity(2, 100, 3, 4)),
        "CNT3" to mapOf("All" to VaccineApiEntity(3, 1000, 5, 6)),
    )

    private val countryDbEntityListAllNoFavorite = listOf(
        CountryDbEntity(1, "CNT1", 10, 1, 2, false),
        CountryDbEntity(2, "CNT2", 100, 3, 4, false),
        CountryDbEntity(3, "CNT3", 1000, 5, 6, false),
    )

    private val countryDbEntityList = listOf(
        CountryDbEntity(1, "CNT1", 10, 1, 2, false),
        CountryDbEntity(2, "CNT2", 100, 3, 4, true),
        CountryDbEntity(3, "CNT3", 1000, 5, 6, false),
    )

    private val countryList = listOf(
        Country(1, "CNT1", 10, 1, 2, false),
        Country(2, "CNT2", 100, 3, 4, true),
        Country(3, "CNT3", 1000, 5, 6, false),
    )

    private val favoriteUid3CountryDbEntity = CountryDbEntity(3, "CNT3", 1000, 5, 6, true)
}
