package me.admund.nmn.ui.main

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import me.admund.nmn.domain.Country
import me.admund.nmn.domain.CountryRepository
import me.admund.nmn.ui.main.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    private lateinit var tested: MainViewModel

    private val testScope = TestCoroutineScope()
    private val countryRepository = mockk<CountryRepository>()

    @Before
    fun setup() {
        every { countryRepository.fetchCountries(any(), any()) } returns Unit
    }

    @Test
    fun `test if all countries will be returned when showOnlyFavorite is false`() =
        runBlockingTest {
            mockCountriesFlow(countryListSample)

            tested = MainViewModel(countryRepository, testScope)
            tested.showOnlyFavorite(false)

            tested.countries().test {
                assertEquals(countryListSample, awaitItem())
            }
        }

    @Test
    fun `test if only favorite countries will be returned when showOnlyFavorite is true`() =
        runBlockingTest {
            mockCountriesFlow(countryListSample)

            tested = MainViewModel(countryRepository, testScope)
            tested.showOnlyFavorite(true)

            tested.countries().test {
                assertEquals(countryListSampleOnlyFavorite, awaitItem())
            }
        }

    private fun mockCountriesFlow(countryList: List<Country>) {
        every { countryRepository.countries() } returns flow {
            emit(countryList)
        }
    }

    private val countryListSample = listOf(
        Country(1, "CNT1", 10, 3, 4, false),
        Country(2, "CNT2", 10, 3, 4, true),
        Country(3, "CNT3", 10, 3, 4, false),
    )

    private val countryListSampleOnlyFavorite = listOf(
        Country(2, "CNT2", 10, 3, 4, true),
    )
}
