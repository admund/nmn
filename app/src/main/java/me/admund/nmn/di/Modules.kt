package me.admund.nmn.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import me.admund.nmn.data.api.VaccinesApi
import me.admund.nmn.data.db.CountriesDatabase
import me.admund.nmn.domain.CountriesRepository
import me.admund.nmn.domain.CountriesRepositoryImpl
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { ViewModelProviderFactory(get()) }
    single<CountriesRepository> { CountriesRepositoryImpl(get(), get()) }
}

val roomModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            CountriesDatabase::class.java,
            CountriesDatabase.DATABASE_NAME
        ).build().countriesDao()
    }
}

val retrofitModule = module {
    single<VaccinesApi> {
        val okHttp = OkHttpClient.Builder().build()
        val gsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(VaccinesApi.BASE_URL)
            .addConverterFactory(gsonConverter)
            .client(okHttp)
            .build()

        retrofit.create(VaccinesApi::class.java)
    }
}
