package me.admund.nmn.di

import androidx.room.Room
import me.admund.nmn.data.db.CountriesDao
import me.admund.nmn.data.db.CountriesDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

//val roomModule = module {
//    single {
//        Room.databaseBuilder(
//            androidApplication(),
//            CountriesDatabase::class.java,
//            CountriesDatabase.DATABASE_NAME
//        ).build().countriesDao()
//    }
//}
