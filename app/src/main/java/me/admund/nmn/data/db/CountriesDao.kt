package me.admund.nmn.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDao {

    @Insert
    suspend fun insertAll(countries: List<CountryDbEntity>)

    @Update
    suspend fun updateAll(countries: List<CountryDbEntity>): Int

    @Update
    suspend fun update(countryDbEntity: CountryDbEntity)

    @Query("select * from countries order by name asc")
    fun queryAllCountries(): Flow<List<CountryDbEntity>>
}
