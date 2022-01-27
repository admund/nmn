package me.admund.nmn.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.admund.nmn.databinding.ActivityMainBinding
import me.admund.nmn.di.ViewModelProviderFactory
import org.koin.android.ext.android.inject

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val factory: ViewModelProviderFactory by inject()
    private val viewModel: MainViewModel by viewModels { factory }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val countriesAdapter = CountriesAdapter { uid ->
            viewModel.updateCountryFavoriteStatus(uid)
        }
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            countriesRecyclerView.adapter = countriesAdapter

            val isShowOnlyFavorites = isShowOnlyFavorites()
            showFavoritesOnlyCheckBox.isChecked = isShowOnlyFavorites
            viewModel.showOnlyFavorite(isShowOnlyFavorites)

            showFavoritesOnlyCheckBox.setOnCheckedChangeListener { _, isChecked ->
                setShowOnlyFavorites(isChecked)
                viewModel.showOnlyFavorite(isChecked)
            }
        }

        viewModel.countries().onEach { list ->
            Log.e("ZXC", "MainActivity: country: ${list.size}")
            countriesAdapter.submitList(list)
        }.launchIn(lifecycleScope)
    }

    private fun isShowOnlyFavorites() =
        getSharedPreferences(SETTINGS, MODE_PRIVATE).getBoolean(SHOW_ONLY_FAVORITES, false)

    private fun setShowOnlyFavorites(value: Boolean) {
        getSharedPreferences(SETTINGS, MODE_PRIVATE).edit()
            .putBoolean(SHOW_ONLY_FAVORITES, value).apply()
    }

    companion object {
        private const val SETTINGS = "SETTINGS"
        private const val SHOW_ONLY_FAVORITES = "SHOW_ONLY_FAVORITES"
    }
}
