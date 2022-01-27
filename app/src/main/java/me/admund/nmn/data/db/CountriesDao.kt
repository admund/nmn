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


//    @Query("select title from goals where uid = :goalUid")
//    fun getGoalTitle(goalUid: Long): String
//
//    @Query("select * from goals order by uid asc")
//    fun getAllGoalEntities(): Flow<List<GoalEntity>>
//
//    @Query("select * from goals where type = :goalType order by uid asc")
//    fun getFilteredGoalEntities(goalType: GoalType): Flow<List<GoalEntity>>
}
