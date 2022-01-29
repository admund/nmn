package me.admund.nmn.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CountryDbEntity::class], version = 1)
abstract class CountriesDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao

    companion object {
        const val DATABASE_NAME = "countries_db"
    }
}
